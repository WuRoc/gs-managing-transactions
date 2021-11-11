package com.example.managingtransactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @ClassName BookingService
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-10 16:23
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@Component
public class BookingService {
    public static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final JdbcTemplate jdbcTemplate;

    public BookingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @Description: 这个方法用@Transactional 标记，意味着任何失败都会导致整个操作回滚到之前的状态并重新抛出原始异常。
     * @Param: [persons] 这意味着如果添加一个人失败，则不会将任何人添加到 BOOKINGS。
     * @return: void
     * @Author: XiaoShuMu
     * @Date: 2021/11/10
     */
    @Transactional
    public void book(String... persons) {
        for (String person : persons) {
            logger.info("Booking " + person + " in a sent...");
            jdbcTemplate.update("insert into BOOKINGS(FIRST_NAME) values (?)", person);
        }
    }

    public List<String> findAllBookings() {
        return jdbcTemplate.query("select FIRST_NAME from BOOKINGS", (rs, rowNum) ->
                rs.getString("FIRST_NAME")
        );
    }

}
