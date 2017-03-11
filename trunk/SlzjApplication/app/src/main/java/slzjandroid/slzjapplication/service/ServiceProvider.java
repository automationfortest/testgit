package slzjandroid.slzjapplication.service;

import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import slzjandroid.slzjapplication.lang.Constant;

public class ServiceProvider {


    /**
     * 请求拦截器
     */
    private final static RestRequestInterceptor restRequestInterceptor = new RestRequestInterceptor();

    private final static RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(Constant.API_URL)
            .setConverter(new GsonConverter(new GsonBuilder().create()))
            .setErrorHandler(new RestErrorHandler())
            .setRequestInterceptor(restRequestInterceptor)
            .build();


//    private final static RestAdapter restdata = new RestAdapter.Builder()
//            .setLogLevel(RestAdapter.LogLevel.FULL)
//            .setEndpoint(Constant.API_WEI_URL)
//            .setConverter(new GsonConverter(new GsonBuilder().create()))
//            .setErrorHandler(new RestErrorHandler())
//            .setRequestInterceptor(restRequestInterceptor)
//            .build();


    public final static SmsService smsService = restAdapter.create(SmsService.class);
    public final static LoginService loginService = restAdapter.create(LoginService.class);
    public final static TokenService tokenService = restAdapter.create(TokenService.class);
    public final static OrderTemplateService orderTemplateService = restAdapter.create(OrderTemplateService.class);
    public final static AddressService addressService = restAdapter.create(AddressService.class);
    public final static BudGetService budGetService = restAdapter.create(BudGetService.class);
    public final static CarOrderService carOrderService = restAdapter.create(CarOrderService.class);
    public final static CityService cityService = restAdapter.create(CityService.class);
    public final static OrderHistoryService orderHistoryService = restAdapter.create(OrderHistoryService.class);
    public final static RegisterService registerService = restAdapter.create(RegisterService.class);
    public final static TeamMenagentService teamMenagentService = restAdapter.create(TeamMenagentService.class);
    public final static DeptGetService deptGetService = restAdapter.create(DeptGetService.class);

    public final static CouponService couponservice = restAdapter.create(CouponService.class);
    public final static DeptEditService deptEditService = restAdapter.create(DeptEditService.class);
    public final static PayService payService = restAdapter.create(PayService.class);

    public final static RechargeService rechargeService = restAdapter.create(RechargeService.class);

    public final static EnterpriseInfoService enterpriseInfoService = restAdapter.create(EnterpriseInfoService.class);

    public final static ReceiptService receiptService = restAdapter.create(ReceiptService.class);


    public final static AdviceService adviceService = restAdapter.create(AdviceService.class);

    public final static UpdataVersionService updataVersionService = restAdapter.create(UpdataVersionService.class);

}
