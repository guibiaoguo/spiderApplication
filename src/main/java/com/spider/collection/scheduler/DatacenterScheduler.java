package com.spider.collection.scheduler;

import com.spider.collection.dao.JDBCHelper;
import com.spider.collection.entity.TasksDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2015/11/4.
 */
@ThreadSafe
public class DatacenterScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {
    private BlockingQueue<Request> queue = new LinkedBlockingQueue();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private TasksDTO tasks;

    private JdbcTemplate jdbcTemplate;

    public DatacenterScheduler(TasksDTO tasks) {
        this.tasks = tasks;
        initQueue();
    }

    public void initQueue() {
        try{
//            if(JDBCHelper.getJdbcTemplate("spider") == null)
//                jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
//            else {
//                jdbcTemplate = JDBCHelper.getJdbcTemplate("spider");
//            }
            if(StringUtils.isEmpty(tasks.getSourceTable())) {
                return;
            }
            jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
            StringBuffer buffer = new StringBuffer().append("SELECT * from ").append(tasks.getSourceTable()).append(" where status=0 ");
//            if(id != null)
//                buffer.append(" and id != '" ).append(id).append("'");
            buffer.append(" LIMIT 0,1000");
            List<Map<String, Object>> results = jdbcTemplate.queryForList(buffer.toString());
            for (Map<String, Object> result : results) {
//                pushWhenNoDuplicate(URLEncoder.encode(StringUtils.trim(result.get("cname").toString()), "utf-8"));
                Request request = new Request();
                request.setUrl(result.get("url").toString());
                request.putExtra("id",result.get("id"));
                request.putExtra("$update",0);
                request.putExtra("$query",0);
                request.putExtra("$table",result);
                pushWhenNoDuplicate(request,null);
                StringBuffer sql = new StringBuffer().append("update ").append(tasks.getSourceTable()).append(" set run_status = 1 ").append(" where id = ? ");
                jdbcTemplate.update(sql.toString(),result.get("id"));
            }
        }catch ( Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
    public void pushWhenNoDuplicate(Request request, Task task) {
        this.queue.add(request);
    }

    public synchronized Request poll(Task task) {
        Request request = this.queue.poll();;
        if(StringUtils.isEmpty(tasks.getSourceTable())) {
            return request;
        }
        System.out.println("================="+getLeftRequestsCount(task));
        Random random = new Random();
        while (request == null) {
            try{
                int num = random.nextInt(10)+1;
                logger.info("喝杯咖啡，休息" + 5 * num + "分钟，再回来");
                Thread.sleep(300000 * num);
                initQueue();
                request = this.queue.poll();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(getLeftRequestsCount(task) == 0) {
            try{
                initQueue();
                int num = random.nextInt(10) + 1;
                System.out.println("喝杯茶，休息"+1*num+"分钟后，再开始工作");
                Thread.sleep(60000 * (random.nextInt(10)+1));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return request;
    }

    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }
}
