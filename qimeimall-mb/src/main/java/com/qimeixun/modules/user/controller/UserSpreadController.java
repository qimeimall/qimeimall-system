package com.qimeixun.modules.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.base.BaseController;
import com.qimeixun.constant.SystemConfigConstants;
import com.qimeixun.entity.Customer;
import com.qimeixun.modules.user.service.UserService;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.MySpreadRO;
import com.qimeixun.ro.UserPageRO;
import com.qimeixun.ro.WithdrawalRO;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.utils.QRCodeUtils;
import com.qimeixun.vo.BrokerageVO;
import com.qimeixun.vo.ResponseResultVO;
import com.quaint.poster.core.impl.PosterDefaultImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "海报")
@RestController
@RequestMapping(value = "/posters", produces = "application/json;charset=UTF-8")
@CrossOrigin
public class UserSpreadController extends BaseController {

    @Resource
    UserService userService;

    @Resource
    SysConfigService sysConfigService;

    @Resource
    TokenUtil tokenUtil;

    @ApiOperation(value = "生成海报", notes = "生成海报")
    @RequestMapping(value = "/createPosters", method = RequestMethod.GET)
    public ResponseResultVO createPosters() throws Exception {
        Customer customer = userService.getCustomerById(tokenUtil.getUserIdByToken());

        String spreadUrl = sysConfigService.getSysConfigValueFromRedis("spread_url");
        BufferedImage background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("static/yaoqing.jpg"));
        BufferedImage bufferedImage = QRCodeUtils.getEncode(spreadUrl + "?spread=" + customer.getReferrerCode());
        SamplePoster poster = SamplePoster.builder()
                .backgroundImage(background)
                //.head(head)
                .nickName(customer.getNickName() + "，邀您加入")
                .mainImage(bufferedImage)// 二维码
                .build();
        PosterDefaultImpl<SamplePoster> impl = new PosterDefaultImpl<>();
        BufferedImage test = impl.annotationDrawPoster(poster).draw(null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(test, "png", stream);
        String base64 = Base64.getEncoder().encodeToString(stream.toByteArray());
        base64 = "data:image/jpg;base64," + base64;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("path", base64);
        return ResponseResultVO.successResult(resultMap);
    }

    @ApiOperation(value = "查询我的代理", notes = "查询我的代理")
    @RequestMapping(value = "/selectMySpreadList", method = RequestMethod.POST)
    public ResponseResultVO selectMySpreadList(@RequestBody MySpreadRO mySpreadRO) {
        if (StrUtil.isBlank(mySpreadRO.getType())) {
            return ResponseResultVO.failResult("查询参数不正确");
        }
        return userService.selectMySpreadList(mySpreadRO);
    }

    @ApiOperation(value = "查询佣金明细", notes = "查询佣金明细")
    @RequestMapping(value = "/selectMyBrokerageList", method = RequestMethod.POST)
    public ResponseResultVO selectMyBrokerageList(@RequestBody UserPageRO pageRO) {
        Page page = new Page<Map<String, Object>>(pageRO.getCurrentPage(), pageRO.getPageSize());
        IPage<BrokerageVO> iPage = userService.selectMyBrokerageList(page, pageRO);
        return getPageObject(iPage, pageRO);
    }

    @ApiOperation(value = "提交提现申请", notes = "提交提现申请")
    @RequestMapping(value = "/addWithdrawal", method = RequestMethod.POST)
    public ResponseResultVO addWithdrawal(@RequestBody WithdrawalRO withdrawalRO) {
        if(StrUtil.isBlank(withdrawalRO.getAccount())){
            return ResponseResultVO.failResult("请填写账号");
        }
        if(StrUtil.isBlank(withdrawalRO.getType())){
            return ResponseResultVO.failResult("请选择提现类型");
        }
        if("2".equals(withdrawalRO.getType()) && StrUtil.isBlank(withdrawalRO.getAccountName())){
            //支付宝
            return ResponseResultVO.failResult("支付宝请输入用户名");
        }
        BigDecimal min = new BigDecimal(0);
        String withdrawalMin = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.WITHDRAW_MIN);
        if(StrUtil.isNotBlank(withdrawalMin)){
            min = new BigDecimal(withdrawalMin);
        }

        if(min.compareTo(withdrawalRO.getMoney()) == 1){ // 提现最小值大于 要提现的金额
            return ResponseResultVO.failResult("提现金额未达到限制");
        }

        WithdrawalDTO dto = new WithdrawalDTO();
        dto.setAccount(withdrawalRO.getAccount());
        dto.setType(withdrawalRO.getType());
        dto.setMoney(withdrawalRO.getMoney());
        dto.setUserId(tokenUtil.getUserIdByToken());
        if("2".equals(withdrawalRO.getType())){
            dto.setAccountName(withdrawalRO.getAccount());
        }
        userService.insertWithdrawal(dto);
        return ResponseResultVO.successResult("success");
    }

    @ApiOperation(value = "查询提现记录", notes = "查询提现记录")
    @RequestMapping(value = "/selectMyWithdrawalList", method = RequestMethod.POST)
    public ResponseResultVO selectMyWithdrawalList(@RequestBody UserPageRO userPageRO) {
        Page page = new Page<Map<String, Object>>(userPageRO.getCurrentPage(), userPageRO.getPageSize());
        IPage<WithdrawalDTO> iPage = userService.selectMyWithdrawalList(page, userPageRO);
        return getPageObject(iPage, userPageRO);
    }

}
