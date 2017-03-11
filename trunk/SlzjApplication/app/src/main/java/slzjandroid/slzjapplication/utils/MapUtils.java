package slzjandroid.slzjapplication.utils;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyifei on 16/4/27.
 */
public class MapUtils {

    private static final double EARTH_RADIUS = 6378137;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private static List<LatLng> latLngs = new ArrayList<>();


    static boolean flag;

    static boolean tag;

    /**
     * 根据点获取图标转的角度
     */
    public static double getAngles(int startIndex, Polyline mVirtureRoad) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }


    /**
     * 根据两点算取图标转的角度
     */
    public static double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    public static double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算取斜率
     */
    public static double getSlope(Polyline mVirtureRoad, int startIndex) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getSlope(startPoint, endPoint);
    }

    /**
     * 算斜率
     */
    public static double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 计算x方向每次移动的距离
     */
    public static double getXMoveDistance(double DISTANCE, double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;

    }

    /**
     * 8.     * 根据两点间经纬度坐标（double值），计算两点间距离，
     * 9.     *
     * 10.     * @param lat1
     * 11.     * @param lng1
     * 12.     * @param lat2
     * 13.     * @param lng2
     * 14.     * @return 距离：单位为米
     * 15.
     */
    public static double DistanceOfTwoPoints(double lat1, double lng1,
                                             double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /**
     * 经纬度每次只保存开始和结束两个点
     *
     * @return
     */
    public static List<LatLng> setLatLngs(LatLng latLng) {
        if (!flag) {
            flag = true;
            if (latLngs != null) {
                latLngs.clear();
            }
            latLngs.add(0, latLng);
            latLngs.add(1, latLng);
        } else {
            if (!tag) {
                tag = true;
            } else {
                LatLng _latLng = latLngs.get(1);
                latLngs.remove(0);
                latLngs.add(0, _latLng);
            }
            latLngs.remove(1);
            latLngs.add(1, latLng);
        }
        return latLngs;
    }

}
