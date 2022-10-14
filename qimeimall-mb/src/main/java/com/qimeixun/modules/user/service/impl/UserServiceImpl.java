package com.qimeixun.modules.user.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.config.Audience;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.Customer;
import com.qimeixun.enums.PointsBillEnum;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.*;
import com.qimeixun.modules.config.WxPayConfiguration;
import com.qimeixun.modules.user.service.UserService;
import com.qimeixun.po.*;
import com.qimeixun.ro.*;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.*;
import com.qimeixun.utils.ShareCodeUtil;
import com.qimeixun.vo.BrokerageVO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.UserPointsVO;
import com.qimeixun.vo.WXSessionModelVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenshouyang
 * @date 2020/5/2218:04
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${user.token.timeout}")
    private Integer tokenTimeout;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    TokenUtil tokenUtil;

    @Resource
    MyServiceMapper myServiceMapper;

    @Resource
    AliSmsUtil aliSmsUtil;

    @Resource
    SysConfigService sysConfigService;

    @Resource
    Audience audience;

    @Resource
    WithdrawalMapper withdrawalMapper;

    @Resource
    PointsRuleMapper pointsRuleMapper;

    @Resource
    UserSignMapper userSignMapper;

    @Resource
    UserPointsBillMapper userPointsBillMapper;

    @Override
    public Map getUserInfo(UserLoginRO userLoginRO) {
        if (SystemConstant.USER_LOGIN_TYPE.equals(userLoginRO.getType())) {
            //请求参数
            String params = "appid=" + WxPayConfiguration.getAppId() + "&secret=" + WxPayConfiguration.getAppSecret() + "&js_code=" + userLoginRO.getCode() + "&grant_type=authorization_code";
            String result = httpClientSend("https://api.weixin.qq.com/sns/jscode2session" + "?" + params);

            Map<String, Object> map = new HashMap<>();
            if (StringUtils.isNotEmpty(result)) {
                String openId = JSON.parseObject(result).getString("openid");
                QueryWrapper<Customer> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("open_id", openId);
                Customer customer = customerMapper.selectOne(userQueryWrapper);
                if (customer == null) {
                    ////用户为空需要新建用户
                    customer = new Customer();
                    customer.setNickName(userLoginRO.getNickName());
                    customer.setHeadImg(userLoginRO.getHeadImg());
                    customer.setFromType("1");
                    customer.setSex(userLoginRO.getSex());
                    customer.setOpenId(openId);
                    customer.setPhone(userLoginRO.getPhone());



                    customerMapper.insert(customer);
                }

                if (StrUtil.isNotBlank(userLoginRO.getReferrerCode()) && customer.getReferrerId() != null) {
                    //绑定邀请人
                    Customer referCustomer = customerMapper.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getReferrerCode, userLoginRO.getReferrerCode()));
                    if (referCustomer != null && referCustomer.getId().longValue() != customer.getId().longValue()) {
                        customer.setReferrerId(referCustomer.getId());
                    }
                }

                customer.setLastLoginTime(new Date());

                customerMapper.updateById(customer);

                map.put(SystemConstant.REDIS_USER_INFO_KEY, customer);
                // 生成登录token
                String token = Tool.getPrimaryKey();

                map.put(SystemConstant.REDIS_USER_TYPE_KEY, SystemConstant.REDIS_MEMBER_USER_TYPE);
                // 缓存key
                final String tokenKey = "token:";
                String tokenRedisKey = tokenKey + token;

                map.put("token", token);
                this.redisTemplate.opsForHash().putAll(tokenRedisKey, map);
                this.redisTemplate.expire(tokenRedisKey, this.tokenTimeout, TimeUnit.DAYS);

                return map;

            } else {
                throw new ServiceException("登录失败");
            }

        }
        return null;
    }

    @Override
    public Map selectMyInfo() {
        String userId = tokenUtil.getUserIdByToken();
        return customerMapper.selectMyInfo(userId);
    }

    @Override
    public List<MyServiceDTO> getMyServiceList() {
        QueryWrapper<MyServiceDTO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 0);
        return myServiceMapper.selectList(queryWrapper);
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @Override
    public void sendCode(String phone) {
        try {
            aliSmsUtil.sendSms(phone, sysConfigService.getSysConfigValueFromRedis(SystemConstant.ACCESS_KEY_ID),
                    sysConfigService.getSysConfigValueFromRedis(SystemConstant.ACCESS_KEY_SECRET));
        } catch (ClientException e) {
            throw new ServiceException("发送失败");
        }
    }

    @Override
    public Map login(PhoneLoginRO phoneLoginRO) {
        /*if (!aliSmsUtil.checkPhoneCode(phoneLoginRO.getPhone(), phoneLoginRO.getCode())) {
            throw new ServiceException("验证码错误");
        }*/
        String openId = "";
        if (SystemConstant.LOGIN_TYPE_WX_MP.equals(phoneLoginRO.getLoginType())) {
            if (StrUtil.isBlank(phoneLoginRO.getWxCode())) {
                throw new ServiceException("获取参数失败");
            }
            //先用code获取openId 查询用户是否登录过
            WXSessionModelVO wxSessionModelVO = WxPayConfiguration.getWXSessionModelVO((phoneLoginRO.getWxCode()));
            openId = wxSessionModelVO.getOpenid();
            Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("open_id", wxSessionModelVO.getOpenid()));
            if (customer != null) {
                phoneLoginRO.setPhone(customer.getPhone());
            } else {
                //要重新注册用户 解析手机号
                if (StrUtil.isBlank(phoneLoginRO.getEncryptedData()) || StrUtil.isBlank(phoneLoginRO.getIv())) {
                    throw new ServiceException("参数错误");
                }
                String result = AesCbcUtil.decrypt(phoneLoginRO.getEncryptedData(), phoneLoginRO.getIv(), wxSessionModelVO.getSession_key());
                if (StringUtils.isNotBlank(result)) {
                    JSONObject userInfoJSON = JSONObject.parseObject(result);
                    log.info("userInfoJSON" + userInfoJSON);
                    String phoneNumber = userInfoJSON.get("phoneNumber").toString();
                    if (StringUtils.isNotBlank(phoneNumber)) {
                        phoneLoginRO.setPhone(phoneNumber);
                    }
                }
            }
        } else {
            if (StrUtil.isBlank(phoneLoginRO.getPhone())) {
                throw new ServiceException("手机号不能为空");
            }
            if (!RegexUtils.checkMobile(phoneLoginRO.getPhone())) {
                throw new ServiceException("手机号不正确");
            }
            if (StrUtil.isBlank(phoneLoginRO.getCode())) {
                throw new ServiceException("验证码不能为空");
            }
        }

        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Customer> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phoneLoginRO.getPhone());
        Customer customer = customerMapper.selectOne(userQueryWrapper);
        if (customer == null) {
            //用户不存在，需要注册
            ////用户为空需要新建用户
            customer = new Customer();
            customer.setNickName(phoneLoginRO.getNickName());
            customer.setHeadImg(phoneLoginRO.getHeadImg());
            customer.setFromType("1");
            if (StrUtil.isNotBlank(openId)) {
                customer.setOpenId(openId);
            }
            customer.setPhone(phoneLoginRO.getPhone());
            customerMapper.insert(customer);
        }
        if (StrUtil.isBlank(customer.getReferrerCode())) { //更新推荐码
            customer.setReferrerCode(ShareCodeUtil.toSerialCode(customer.getId()));
        }
        if (StrUtil.isNotBlank(phoneLoginRO.getReferrerCode()) && customer.getReferrerId() != null) {
            //绑定邀请人
            Customer referCustomer = customerMapper.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getReferrerCode, phoneLoginRO.getReferrerCode()));
            if (referCustomer != null && referCustomer.getId().longValue() != customer.getId().longValue() ) {
                customer.setReferrerId(referCustomer.getId());
            }
        }
        //更新最后登录时间
        customer.setLastLoginTime(new Date());
        customerMapper.updateById(customer);
        map.put(SystemConstant.REDIS_USER_INFO_KEY, customer);
        // 生成登录token
        //String token = Tool.getPrimaryKey();
        String token = JwtUtil.TOKEN_PREFIX + JwtUtil.createJWT(String.valueOf(customer.getId()), customer.getPhone(), new ArrayList<String>(), audience);

        map.put("token", token);
        return map;
    }

    /**
     * 查询用户的余额
     *
     * @return
     */
    @Override
    public Map selectUserBalance() {
        Customer customer = customerMapper.selectById(tokenUtil.getUserIdByToken());

        BigDecimal consumption = customerMapper.selectUserConsumptionMoney(tokenUtil.getUserIdByToken());

        //查询用户已经消费的
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balance", customer.getBalance());
        resultMap.put("consumption", consumption);
        return resultMap;
    }

    @Override
    public Customer getCustomerById(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new ServiceException("用户id不能为空");
        }
        return customerMapper.selectById(userId);
    }

    @Override
    public ResponseResultVO selectMySpreadList(MySpreadRO mySpreadRO) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        if ("1".equals(mySpreadRO.getType())) {
            queryWrapper.lambda().eq(Customer::getReferrerId, tokenUtil.getUserIdByToken()).orderByDesc(Customer::getCreateTime);
        } else if ("2".equals(mySpreadRO.getType())) {
            List<Customer> customers = customerMapper.selectList(new QueryWrapper<Customer>().lambda().eq(Customer::getReferrerId, tokenUtil.getUserIdByToken()));
            List<Long> ids = customers.stream().map(Customer::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ids)) {
                return ResponseResultVO.resultEmptyList(mySpreadRO);
            }
            queryWrapper.lambda().in(Customer::getReferrerId, ids).orderByDesc(Customer::getCreateTime);
        } else {
            return ResponseResultVO.failResult("参数不正确");
        }
        IPage<Customer> page = customerMapper.selectPage(new Page<>(mySpreadRO.getCurrentPage(), mySpreadRO.getPageSize()), queryWrapper);
        return ResponseResultVO.resultList(page.getRecords(), (int) page.getTotal(), mySpreadRO);
    }

    /**
     * 查询我的佣金记录
     *
     * @param page
     * @param pageRO
     * @return
     */
    @Override
    public IPage<BrokerageVO> selectMyBrokerageList(Page page, UserPageRO pageRO) {
        pageRO.setUserId(tokenUtil.getUserIdByToken());
        return customerMapper.selectMyBrokerageList(page, pageRO);
    }

    @Override
    public synchronized void insertWithdrawal(WithdrawalDTO dto) {
        //查询用户是否还有待审核的记录
        Integer count = withdrawalMapper.selectCount(new QueryWrapper<WithdrawalDTO>().eq("user_id", dto.getUserId()).eq("status", "0"));
        if (count.intValue() > 0) {
            throw new ServiceException("已有待审核提现申请，请等待审核");
        }
        dto.setId(IdUtil.getSnowflake(1, 1).nextIdStr());
        withdrawalMapper.insert(dto);
    }

    @Override
    public IPage<WithdrawalDTO> selectMyWithdrawalList(Page page, UserPageRO userPageRO) {
        QueryWrapper<WithdrawalDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", tokenUtil.getUserIdByToken());
        wrapper.orderByDesc("create_time");
        return withdrawalMapper.selectPage(page, wrapper);
    }

    /**
     * 查询签到信息
     *
     * @return
     */
    @Override
    public UserPointsVO selectUserSignList() {
        //查询我的积分
        Customer customer = customerMapper.selectById(tokenUtil.getUserIdByToken());

        List<PointsRuleDTO> ruleDTOS = pointsRuleMapper.selectList(new QueryWrapper<PointsRuleDTO>().lambda().eq(PointsRuleDTO::getIsShow, "0").orderByAsc(PointsRuleDTO::getSortNo));

        if (CollectionUtils.isEmpty(ruleDTOS)) {
            throw new ServiceException("未开启签到送积分策略");
        }

        int index = 0; //当前签到了第几个
        int todayIsSign = 0; //今天是否签到
        int signCount = 0; // 连续签到几天

        boolean yesterdayIsSign = checkYesterdayIsSign();

        boolean todayIsSignBoolean = checkTodayIsSign();
        if (yesterdayIsSign) { //昨天是否签到
            if (customer.getSignCount() >= ruleDTOS.size()) {
                index = ruleDTOS.size() - 1;
                if (todayIsSignBoolean) {
                    index++;
                }
            } else {
                index = customer.getSignCount();
            }
            signCount = customer.getSignCount();
        }

        //查询 今天是否签到
        if (todayIsSignBoolean) {
            todayIsSign = 1;
            signCount = customer.getSignCount();

            if (!yesterdayIsSign) {
                index = 1;
            }
        }

        //查询用户累计签到多少次
        Integer userSignCount = userSignMapper.selectCount(new QueryWrapper<UserSignDTO>().lambda().eq(UserSignDTO::getUserId, tokenUtil.getUserIdByToken()));

        //查询一共有多少个用户参与
        QueryWrapper<UserSignDTO> wrapper = new QueryWrapper<>();
        wrapper.select("distinct user_id").lambda().groupBy(UserSignDTO::getUserId);
        Integer totalUserSign = userSignMapper.selectCount(wrapper);


        UserPointsVO userPointsVO = new UserPointsVO();
        userPointsVO.setPoints(customer.getPoints());
        userPointsVO.setRuleDTOS(ruleDTOS);
        userPointsVO.setIndex(index);
        userPointsVO.setTodayIsSign(todayIsSign);
        userPointsVO.setUserSignCount(userSignCount == null ? 0 : userSignCount);
        userPointsVO.setTotalUserSign(totalUserSign == null ? 0 : totalUserSign);
        userPointsVO.setSignCount(signCount);

        return userPointsVO;
    }

    private boolean checkTodayIsSign() {
        Date date = new Date();
        UserSignDTO userSignDTO = userSignMapper.selectOne(new QueryWrapper<UserSignDTO>().lambda().eq(UserSignDTO::getUserId, tokenUtil.getUserIdByToken()).orderByDesc(UserSignDTO::getCreateTime).last("limit 1"));
        if (userSignDTO != null) {
            if (DateUtil.getDayBeginTime(date).getTime() == DateUtil.getDayBeginTime(userSignDTO.getCreateTime()).getTime()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkYesterdayIsSign() {
        Date date = new Date();
        Date yesterday = DateUtil.addDateDay(date, -1);
        QueryWrapper<UserSignDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserSignDTO::getUserId, tokenUtil.getUserIdByToken()).lt(UserSignDTO::getCreateTime, DateUtil.getDayBeginTime(date))
                .ge(UserSignDTO::getCreateTime, DateUtil.getDayBeginTime(yesterday));
        int count = userSignMapper.selectCount(queryWrapper);
        if (count > 0) return true;
        return false;
    }

    /**
     * 签到
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSignRecord() {

        Customer customer = customerMapper.selectById(tokenUtil.getUserIdByToken());

        List<PointsRuleDTO> ruleDTOS = pointsRuleMapper.selectList(new QueryWrapper<PointsRuleDTO>().lambda().eq(PointsRuleDTO::getIsShow, "0").orderByAsc(PointsRuleDTO::getSortNo));

        if (CollectionUtils.isEmpty(ruleDTOS)) {
            throw new ServiceException("未开启签到送积分策略");
        }

        if (checkTodayIsSign()) {
            throw new ServiceException("今天已经签到啦");
        }

        int index = 0; //当前签到了第几个
        if (checkYesterdayIsSign()) { //昨天是否签到
            if (customer.getSignCount() >= ruleDTOS.size()) {
                index = ruleDTOS.size() - 1;
            } else {
                index = customer.getSignCount();
            }
        } else {
            customer.setSignCount(0);
        }

        UserSignDTO signDTO = new UserSignDTO();

        signDTO.setTitle("签到奖励");
        signDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        signDTO.setNumber(ruleDTOS.get(index).getPoints());
        signDTO.setCreateTime(new Date());
        userSignMapper.insert(signDTO);

        //给用户增加积分
        Customer user = new Customer();
        user.setId(customer.getId());
        user.setPoints(customer.getPoints().add(ruleDTOS.get(index).getPoints()));
        user.setSignCount(customer.getSignCount() + 1);
        customerMapper.updateById(user);

        //增加积分账单记录
        UserPointsBillDTO pointsBillDTO = new UserPointsBillDTO();
        pointsBillDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        pointsBillDTO.setMoney(ruleDTOS.get(index).getPoints());
        pointsBillDTO.setType(PointsBillEnum.TYPE_SIGN.getType());
        pointsBillDTO.setRemark(PointsBillEnum.TYPE_SIGN.getRemark());
        userPointsBillMapper.insert(pointsBillDTO);
    }

    @Override
    public IPage selectUserPointsList(PageRO pageRO) {
        QueryWrapper<UserPointsBillDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserPointsBillDTO::getUserId, tokenUtil.getUserIdByToken()).orderByDesc(UserPointsBillDTO::getCreateTime);
        IPage iPage = new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize());
        return userPointsBillMapper.selectPage(iPage, queryWrapper);
    }

    /**
     * 微信访问获取结果
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpClientSend(String url) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            return response;
        } catch (Exception ex) {
            return null;
        }
    }
}
