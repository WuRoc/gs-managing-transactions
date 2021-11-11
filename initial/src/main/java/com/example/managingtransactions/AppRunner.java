package com.example.managingtransactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @ClassName AppRunner
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-11 10:11
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@Component
public class AppRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);


    private final BookingService bookingService;

    public AppRunner(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        bookingService.book("Alice", "Wan", "YaWen");
        Assert.isTrue(bookingService.findAllBookings().size() == 3, "第一次预订没有问题");
        logger.info("Alice, WanHui, YaWen 已经被订");

        try {
            bookingService.book("Chris", "Samuel");
        } catch (RuntimeException e) {
            logger.info("v--- 以下异常是期望的，因为 'Samuel' 太大 对数据库来说 ---v");
            logger.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            logger.info("至今" + person + " 被预订。");
        }

        logger.info("你不应该看到 Chris 或 Samuel 违反了数据库约束 和 Chris 在同一个 TX 中回滚");

        Assert.isTrue(bookingService.findAllBookings().size() == 3, "Samuel  应该触发回滚");

        try {
            bookingService.book("Buddy", null);
        } catch (RuntimeException e) {
            logger.error("v--- 以下异常是期望的，因为 null 不是对数据库有效 ---v");
            logger.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            logger.info("至今" + person + " 被预订。");
        }

        logger.info("你不应该看到Buddy或者null。 null 违反数据库约束，Buddy 是被回滚在相同的TX");
        //这个expression必须是正确的，否则会抛异常
        Assert.isTrue(bookingService.findAllBookings().size() == 3, "null 应该触发回滚");

    }
}
