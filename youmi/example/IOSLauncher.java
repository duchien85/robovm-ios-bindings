package net.mwply.bangdings;

import java.util.ArrayList;
import java.util.List;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSMutableAttributedString;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSRange;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.NSAttributedStringAttribute;
import org.robovm.apple.uikit.NSUnderlineStyle;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UIGraphics;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.NotImplemented;
import org.robovm.objc.block.VoidBlock;
import org.robovm.objc.block.VoidBlock1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;

import net.mwplay.admob.GADAdSize;
import net.mwplay.admob.GADBannerView;
import net.mwplay.admob.GADInterstitial;
import net.mwplay.admob.GADInterstitialDelegateAdapter;
import net.mwplay.admob.GADRequest;
import net.mwplay.admob.GADRequestError;
import net.mwplay.bmob.Bmob;
import net.mwplay.bmob.BmobBooleanResultBlock;
import net.mwplay.bmob.BmobObject;
import net.mwplay.bmob.BmobObjectResultBlock;
import net.mwplay.bmob.BmobQuery;
import net.mwplay.umenganalysics.MobClick;
import net.mwplay.umenganalysics.MobClickGameAnalytics;
import net.mwplay.umenganalysics.MobClickSocialAnalytics;
import net.mwplay.umenganalysics.MobClickSocialWeibo;
import net.mwplay.umenganalysics.ReportPolicy;
import net.mwplay.youmi.PublicCallConfig;
import net.mwplay.youmi.PublicCallList;
import net.mwplay.youmi.PublicCallPointsManager;
import net.mwply.bangdings.GdxGame;

public class IOSLauncher extends IOSApplication.Delegate implements ITools {
	private static final String AD_UNIT_ID = "ca-app-pub-1743332321423234/7283499900";
	static IOSLauncher instance;

	@Override
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		return new IOSApplication(new GdxGame(instance), config);
	}

	public static void main(String[] argv) {
		instance = new IOSLauncher();
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}

	@Override
	public boolean didFinishLaunching(UIApplication application,
			UIApplicationLaunchOptions launchOptions) {
		// Bmob.registerWithAppKey("34b7704a3b372576a732b7675abce9e8");
		// showInterAd();

		boolean is = super.didFinishLaunching(application, launchOptions);

		// add();

		youmi();
		return is;
	}

	private void youmi() {
		//初始化
		PublicCallConfig.PublicCallLaunchWithAppID("2eb39466ebdcaa64",
				"89d4c4baf4a92078");
		
		//积分墙,推广墙初始化
		PublicCallList.PublicCallMyEnable();
		//展示积分墙
		PublicCallList.PublicCallShowOffers(true, new Runnable() {

			@Override
			public void run() {
				System.out.println("youmi show");
			}
		}, new Runnable() {

			@Override
			public void run() {
				System.out.println("youmi exit");
			}
		});

		PublicCallList.PublicCallShowOffers(false, new Runnable() {

			@Override
			public void run() {
				System.out.println("youmi tuijian show");
			}
		}, new Runnable() {

			@Override
			public void run() {
				System.out.println("youmi tuijian exit");
			}
		});
		
		//使用积分
		PublicCallPointsManager.PublicCallEnable();
		//打印积分
		int points = PublicCallPointsManager.PublicCallPointsRemained();
		System.out.println("points = " + points);
		//消耗积分
		PublicCallPointsManager.PublicCallDescreesePoints(10);
	}

	private void add() {
		// bmob
		Bmob.registerWithAppKey("34b7704a3b372576a732b7675abce9e8");
		// Gdx.app.log("xxx", "showInterAd");
		System.out.println("showInterAd");
		final BmobObject gameScore = new BmobObject("GameScore");
		gameScore.setObject(new NSString("小明"), "playerName");
		gameScore.setObject(NSNumber.valueOf(100), "playerScore");
		gameScore.setObject(NSNumber.valueOf(true), "cheatMode");

		gameScore.saveInBackgroundWithResultBlock(new BmobBooleanResultBlock() {
			@Override
			public void invoke(boolean isSuccessful, NSError error) {
				if (isSuccessful) {
					System.out.println("success");
					System.out.println(gameScore.getObjectId());
					System.out.println(Bmob.getServerTimestamp());
					query();
				} else {
					System.out.println(error.getErrorCode() + error.getDomain());
				}
			}
		});
		System.out.println("showInterAd ---- end");
	}

	private void query() {
		BmobQuery mBmobQuery = new BmobQuery("GameScore");
		mBmobQuery.getObjectInBackgroundWithId("bad60c9b08",
				new BmobObjectResultBlock() {

					@Override
					public void invoke(BmobObject object, NSError error) {
						String name = object.objectForKey("playerName")
								.toString();
						System.out.println("name = " + name);

						// change(object);
					}
				});
	}

	private void change(final BmobObject object) {
		BmobObject obj = BmobObject.objectWithoutDatatWithClassName(
				object.getClassName(), object.getObjectId());
		obj.setObject(new NSString("改过的小明"), "playerName");
		obj.updateInBackgroundWithResultBlock(new BmobBooleanResultBlock() {

			@Override
			public void invoke(boolean isSuccessful, NSError error) {
				// TODO Auto-generated method stub
				System.out.println("修改成功....");

				delete(object);
			}
		});
	}

	private void delete(BmobObject object) {
		object.deleteInBackgroundWithBlock(new BmobBooleanResultBlock() {

			@Override
			public void invoke(boolean isSuccessful, NSError error) {
				System.out.println("删除成功....");
			}
		});
	}

	@Override
	public void showInterAd() {
		// Bmob.registerWithAppKey("34b7704a3b372576a732b7675abce9e8");
		// //Gdx.app.log("xxx", "showInterAd");
		// System.out.println("showInterAd");
		// final BmobObject gameScore = new BmobObject("GameScore");
		// gameScore.setObject(new NSString("小明"), "playerName");
		// gameScore.setObject(NSNumber.valueOf(100), "playerScore");
		// gameScore.setObject(NSNumber.valueOf(true), "cheatMode");
		//
		// gameScore.saveInBackgroundWithResultBlock(new
		// BmobBooleanResultBlock() {
		// @Override
		// public void invoke(boolean isSuccessful, NSError error) {
		// if (isSuccessful) {
		// System.out.println("success");
		// System.out.println(gameScore.getObjectId());
		// System.out.println(Bmob.getServerTimestamp());
		// }else{
		// System.out.println(error.getErrorCode() + error.getDomain());
		// }
		// }
		// });
		// System.out.println("showInterAd ---- end");
		// Gdx.app.log("xxx", "showInterAd ---- end");

		/*
		 * final GADInterstitial interstitial = new GADInterstitial(AD_UNIT_ID);
		 * interstitial.setDelegate(new GADInterstitialDelegateAdapter(){
		 * 
		 * @Override public void didReceiveAd(GADInterstitial ad) {
		 * super.didReceiveAd(ad); interstitial.present(((IOSApplication)
		 * Gdx.app).getUIViewController()); }
		 * 
		 * @Override public void didFailToReceiveAd(GADInterstitial ad,
		 * GADRequestError error) { Gdx.app.log("xxx", error.getCode() + "");
		 * super.didFailToReceiveAd(ad, error); } });
		 * 
		 * GADRequest request = GADRequest.create(); List<String> devices = new
		 * ArrayList<String>(); devices.add(GADRequest.GAD_SIMULATOR_ID);
		 * 
		 * request.setTestDevices(devices); interstitial.loadRequest(request);
		 */
	}

	@Override
	public void showBanner() {
		/*
		 * final GADBannerView mBannerView = new
		 * GADBannerView(GADAdSize.smartBannerPortrait());
		 * mBannerView.setAdUnitID("ca-app-pub-1743332321423234/2574098708");
		 * 
		 * final UIViewController controller = ((IOSApplication)
		 * Gdx.app).getUIViewController();
		 * 
		 * GADRequest request = GADRequest.create(); List<String> devices = new
		 * ArrayList<String>(); devices.add(GADRequest.GAD_SIMULATOR_ID);
		 * mBannerView.setRootViewController(controller);
		 * controller.getView().addSubview(mBannerView);
		 * 
		 * mBannerView.loadRequest(request);
		 */
	}
}