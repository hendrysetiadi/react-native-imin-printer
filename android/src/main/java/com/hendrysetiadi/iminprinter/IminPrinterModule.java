package com.hendrysetiadi.iminprinter;

import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableArray;
import com.imin.library.SystemPropManager;
import com.imin.printerlib.Callback;
import com.imin.printerlib.IminPrintUtils;
import com.imin.printerlib.print.PrintUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IminPrinterModule extends ReactContextBaseJavaModule {
  public static final String NAME = "IminPrinter";
  private static final String TAG = "IminInnerPrinterModule";
  public static ReactApplicationContext reactApplicationContext;
  private IminPrintUtils mIminPrintUtils;
  private BitmapUtils bitMapUtils;

  public IminPrinterModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactApplicationContext = reactContext;

    mIminPrintUtils = IminPrintUtils.getInstance(reactApplicationContext);
    bitMapUtils = new BitmapUtils(reactApplicationContext);
  }

  @Override
  @NonNull
  public String getName() {
      return NAME;
  }


  @ReactMethod
  public void initPrinter(final Promise promise){
    final IminPrintUtils printUtils = mIminPrintUtils;
    String deviceModel = SystemPropManager.getModel();
    List<String> spiDeviceList = new ArrayList<>(Arrays.asList("M2-202", "M2-203", "M2-Pro"));
    List<String> usbDeviceList = new ArrayList<>(Arrays.asList(
      "S1-701", "S1-702", "D1p-601", "D1p-602", "D1p-603", "D1p-604", "D1w-701", "D1w-702", "D1w-703", "D1w-704",
      "D4-501", "D4-502", "D4-503", "D4-504", "D4-505", "M2-Max", "D1", "D1-Pro", "Swift 1"
    ));

    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          if (spiDeviceList.contains(deviceModel)) {
            printUtils.resetDevice();
            printUtils.initPrinter(IminPrintUtils.PrintConnectType.SPI);
            printUtils.getPrinterStatus(IminPrintUtils.PrintConnectType.SPI, new Callback() {
              @Override
              public void callback(int status) {
                // toast("Print SPI status : "+status);
                if (status == -1 && PrintUtils.getPrintStatus() == -1){
                  promise.reject(""+0, ""+status);
                } else {
                  promise.resolve(status);
                }
              }
            });
          } else if (usbDeviceList.contains(deviceModel)) {
            printUtils.resetDevice();
            printUtils.initPrinter(IminPrintUtils.PrintConnectType.USB);
            int status = printUtils.getPrinterStatus(IminPrintUtils.PrintConnectType.USB);
            // toast("Printer USB Status : "+status);
            promise.resolve(status);
          }
        } catch (Exception e) {
          e.printStackTrace();
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }


  @ReactMethod
  public void printAndLineFeed(final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printAndLineFeed();
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  /**
   * @param height    0 - 255
   * @param promise
   */
  @ReactMethod
  public void printAndFeedPaper(int height, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final int mHeight = height;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printAndFeedPaper(mHeight);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void partialCut(final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.partialCut();
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }


  /**
   * @param alignment   0 = Left, 1 = Center, 2 = Right (default 0)
   * @param promise
   */
  @ReactMethod
  public void setAlignment(int alignment, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final int mAlignment = alignment;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.setAlignment(mAlignment);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  /**
   * @param size      Font Size (default 28)
   * @param promise
   */
  @ReactMethod
  public void setTextSize(int size, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final int mSize = size;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.setTextSize(mSize);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  /**
   * @param style      0 = Normal, 1 = Bold, 2 = Italic, 3 = Bold Italic
   * @param promise
   */
  @ReactMethod
  public void setTextStyle(int style, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final int mStyle = style;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.setTextStyle(mStyle);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }



  @ReactMethod
  public void printText(String text, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final String mText = text;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printText(mText);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printTextWordWrap(String text, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final String mText = text;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printText(mText, 1);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  /**
   * @param textArray       Text Array (String)
   * @param widthArray      Width of each Column, must be greater than 0 (Int)
   * @param alignArray      Alignment Array (0 = Left, 1 = Center, 2 = Right)
   * @param fontSizeArray   Font Size of each Column
   * @param promise
   */
  @ReactMethod
  public void printColumnsText(ReadableArray textArray, ReadableArray widthArray,
                               ReadableArray alignArray, ReadableArray fontSizeArray,
                               final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    final String[] mTextArray = ArrayUtils.toArrayOfString(textArray);
    final int[] mWidthArray = ArrayUtils.toArrayOfInteger(widthArray);
    final int[] mAlignArray = ArrayUtils.toArrayOfInteger(alignArray);
    final int[] mFontSizeArray = ArrayUtils.toArrayOfInteger(fontSizeArray);
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printColumnsText(mTextArray, mWidthArray, mAlignArray, mFontSizeArray);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }

  /**
   * @param data      Base64 Image Data
   * @param width
   * @param height
   * @param promise
   */
  @ReactMethod
  public void printSingleBitmap(String data, int width, int height, final Promise promise) {
    final IminPrintUtils printUtils = mIminPrintUtils;
    byte[] decoded = Base64.decode(data, Base64.DEFAULT);
    final Bitmap bitmap = bitMapUtils.decodeBitmap(decoded, width, height);
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printUtils.printSingleBitmap(bitmap, 1);
          promise.resolve(null);
        } catch (Exception e) {
          e.printStackTrace();
          Log.i(TAG, "ERROR: " + e.getMessage());
          promise.reject("" + 0, e.getMessage());
        }
      }
    });
  }



  protected void toast(String message) {
    Toast.makeText(reactApplicationContext, message, Toast.LENGTH_SHORT).show();
  }
}
