package com.coffeewx.service;

import com.coffeewx.core.Service;
import com.coffeewx.model.WxFansMsgRes;

import java.util.List;

/**
 * Created by CodeGenerator on 2019/03/13.
 */
public interface WxFansMsgResService extends Service<WxFansMsgRes> {

    List<WxFansMsgRes> findList(WxFansMsgRes tWxFansMsgRes);

}
