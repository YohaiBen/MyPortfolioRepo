package app.core;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.core.entities.Coupon;
import app.core.repositories.CouponRepository;

/**
 * @author yohai
 *
 * @summary
 * 
 *          A thread that runs every 24 hours and deleted all the Coupons and
 *          the purchases of Coupons that their EndDate expired from the DB
 * 
 * 
 *
 */
@Component
public class CouponExpirationDailyJob implements Runnable {

	@Autowired
	private CouponRepository couponRepository;
	private boolean quit;
	private Thread dailyJob;

	public CouponExpirationDailyJob() {
		super();
		this.dailyJob = new Thread(this, "Daily Job");
	}

	@PostConstruct
	public void initDailyJob() {
		System.out.println("================================================");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~|| Welcome to Coupons System ||~~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("================================================");
		dailyJob.start();
		System.out.println("*****DailyJob started*****");
	}

	@PreDestroy
	public void stopDailyJob() {
		dailyJob.interrupt();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("================================================");
		System.out.println("====================Good--bye=====================");
		System.out.println("================================================");
	}

	@Override
	@Transactional
	public synchronized void run() {
		try {
			// the time DailyJob should sleep after every iteration - 24 hours
			Long oneDay = TimeUnit.HOURS.toMillis(24);

			while (quit != true) {
				List<Coupon> couponsExpiredList = couponRepository
						.findByEndDateBefore(new Date(new GregorianCalendar().getTimeInMillis()));
				for (Coupon coupon : couponsExpiredList) {

					couponRepository.deleteById(coupon.getCouponId());
					System.out.println("******A Coupon that it's Expiration was End - have been Deleted******");
				}
				try {
					Thread.currentThread().join(oneDay);
					System.out.println("****=======anoter Iteration From Daily Job======****");

				} catch (InterruptedException e) {
					quit = true;
					System.out.println("DailyJob done, the program END");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}