package com.spider.collection.pipeline;

import com.spider.collection.dao.JDBCHelper;
import com.spider.collection.entity.TargetTaskDTO;
import com.spider.collection.entity.TasksDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 */
public class MySqlPipeline implements Pipeline{

    private JdbcTemplate jdbcTemplate;
    private TasksDTO tasks;
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MySqlPipeline() {
        jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
    }

    public MySqlPipeline(TasksDTO tasks) {
        this.tasks = tasks;
        jdbcTemplate = JDBCHelper.createMysqlTemplate("spider","jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8","root","root",1,2);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map map = resultItems.getAll();
        Boolean flag = false;
        List<TargetTaskDTO> targetTasks = tasks.getTargetTaskDTOs();
        if(resultItems.getRequest().getExtra("$query")!=null && StringUtils.equals(resultItems.getRequest().getExtra("$query").toString(),"1")) {
            return;
        }
        int pageSize = Integer.parseInt(resultItems.getRequest().getExtra("$pageSize") != null ?resultItems.getRequest().getExtra("$pageSize").toString():"1");
        for (int i = 0; i < pageSize; i++) {
            flag = false;
            List par = new ArrayList<>();
            StringBuffer sql = new StringBuffer().append("INSERT INTO " + tasks.getSaveTable() + " (");
            StringBuffer params = new StringBuffer();
            StringBuffer val = new StringBuffer().append("VALUES(");
            String priex = "";
            if(i>0) {
                priex = i+"";
            }
            Object temp2 = map.get(targetTasks.get(0).getKey() + priex);
            StringBuffer selctsql = null;
            List results = null;
            if(temp2!=null) {
                selctsql = new StringBuffer().append("SELECT * FROM ").append(tasks.getSaveTable()).append(" where ").append(targetTasks.get(0).getKey()).append(" = ").append(" ? ");
                results = jdbcTemplate.queryForList(selctsql.toString(),temp2);
                if (results == null || results.size() == 0) {
                    for (TargetTaskDTO targetTask : targetTasks) {
                        if(!targetTask.getFlag()) {
                            continue;
                        }
                        Object param = map.get(targetTask.getKey() + priex);
                        params.append(targetTask.getKey()).append(",");
                        if (param != null && StringUtils.isNotEmpty(param.toString())) {
                            flag = true;
                            par.add(param);
//                            val.append("'").append(StringUtils.trim(param)).append("'").append(",");
                        } else {
                            par.add(null);
                        }
                        val.append("?").append(",");
                    }
                    sql.append(StringUtils.substringBeforeLast(params.toString(), ",")).append(") ").append(StringUtils.substringBeforeLast(val.toString(), ",")).append(")");
                    if (flag)
                        jdbcTemplate.update(sql.toString(),par.toArray(new Object[par.size()]));
                }
            }
        }

    }
}
