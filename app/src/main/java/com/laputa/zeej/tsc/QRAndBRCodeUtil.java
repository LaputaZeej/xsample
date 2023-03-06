//package com.laputa.zeej.tsc;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.util.Log;
//
//import com.bugu.ge.scanner.App;
//import com.bugu.ge.scanner.R;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import com.king.zxing.util.CodeUtils;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//
//public class QRAndBRCodeUtil {
//    /**
//     * 生成二维码Bitmap
//     *
//     * @param content   内容
//     * @param widthPix  图片宽度
//     * @param heightPix 图片高度
//     * @param logoBm    二维码中心的Logo图标（可以为null）
//     * @param filePath  用于存储二维码图片的文件路径
//     * @return 生成二维码及保存文件是否成功
//     */
//    public static boolean createQRImage(String content, int widthPix, int heightPix,
//                                        Bitmap logoBm, String filePath) {
//        try {
//            if (content == null || "".equals(content)) {
//                return false;
//            }
//
//            //配置参数
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 1); //default is 4
//
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = null;
//            try {
//                bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
//                        heightPix, hints);
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
//
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
//
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
//
//            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，
//            // 内存消耗巨大！
//            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
//                    new FileOutputStream(filePath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    public static Bitmap createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm) {
//        try {
//            if (content == null || "".equals(content)) {
//                return null;
//            }
//
//            //配置参数
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
////            hints.put(EncodeHintType.QR_VERSION, "2");
//            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 1); //default is 4
//
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = null;
//            try {
//                bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
//                        heightPix, hints);
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
//
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
//
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
//
//            return bitmap;
//
//            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，
//            // 内存消耗巨大！
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public static boolean save(Bitmap bitmap, String filePath) {
//        if (bitmap == null) {
//            return false;
//        }
//        FileOutputStream fileOutputStream = null;
//        try {
//            File file = new File(filePath);
//            fileOutputStream = new FileOutputStream(file);
//            boolean result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            bitmap.recycle();
//        }
//        return false;
//    }
//
//    /**
//     * 在二维码中间添加Logo图案
//     */
//    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
//        if (src == null) {
//            return null;
//        }
//
//        if (logo == null) {
//            return src;
//        }
//
//        //获取图片的宽高
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        int logoWidth = logo.getWidth();
//        int logoHeight = logo.getHeight();
//
//        if (srcWidth == 0 || srcHeight == 0) {
//            return null;
//        }
//
//        if (logoWidth == 0 || logoHeight == 0) {
//            return src;
//        }
//
//        //logo大小为二维码整体大小的1/5
//        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        try {
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(src, 0, 0, null);
//            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
//
////            canvas.save(Canvas.ALL_SAVE_FLAG);
//            canvas.save();//保存
//            canvas.restore();
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }
//
//        return bitmap;
//    }
//
//
//    private static final int BLACK = 0xff000000;
//
//    private static final int WHITE = 0xFFFFFFFF;
//
//    private static final BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
//
//    public static Bitmap createBarCode(String content, int desiredWidth, int desiredHeight) {
////        return ZXingUtil.creatBarcode(App.INSTANCE,content,desiredWidth,desiredHeight,false);
//        // 需要与显示的宽高比一致
//        int w = (int) App.INSTANCE.getResources().getDimension(R.dimen.br_code_width);
//        int h = (int) App.INSTANCE.getResources().getDimension(R.dimen.br_code_height);
//        String debug = "helloworld";
//        return CodeUtils.createBarCode(content, BarcodeFormat.CODE_128, w, h);
//        //return createBarcode(content, barcodeFormat, desiredWidth, desiredHeight);
//    }
//
//    static {
//        Map<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//    }
//
//    public static Map<EncodeHintType, String> createHints() {
//        Map<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        return hints;
//    }
//
//    public static Bitmap createBarCode1(String content, int desiredWidth, int desiredHeight) {
//
//        try {
//
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 3000, 700
//                    , createHints());
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            int[] pixels = new int[width * height];
//            for (int y = 0; y < height; y++) {
//                int offset = y * width;
//                for (int x = 0; x < width; x++) {
//                    pixels[offset + x] = bitMatrix.get(x, y) ? 0xff000000 : 0xFFFFFFFF;
//                }
//            }
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static Bitmap createBarcode(String contents, BarcodeFormat barcodeFormat, int desiredWidth, int desiredHeight) {
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix result = null;
//        Log.d("_brcode_", "createBarcode_" + contents);
//
//        try {
//            result = writer.encode(contents, barcodeFormat, desiredWidth, desiredHeight, createHints());
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        int width;
//        int left = -1, right = -1;
//        for (int i = 0; i < result.getWidth(); i++) {
//            if (result.get(i, 0) && left == -1) {
//                left = i;
//            }
//
//            if (result.get(result.getWidth() - i - 1, 0) && right == -1) {
//                right = result.getWidth() - i - 1;
//            }
//
//            if (left != -1 && right != -1)
//                break;
//        }
//
//        width = right - left + 1;
//        int[] pixels = new int[width];
//        for (int i = 0; i < width; i++) {
//            pixels[i] = result.get(i + left, 0) ? Color.BLACK : Color.WHITE;
//        }
//
//        float scale = desiredWidth / (width * 1F);
//
//        Bitmap bitmap = Bitmap.createBitmap(width, 1, Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, 1);
//        Matrix matrix = new Matrix();
//        matrix.postScale(scale, desiredHeight);
//        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        bitmap.recycle();
//        return resizeBmp;
//    }
//
//
//}
