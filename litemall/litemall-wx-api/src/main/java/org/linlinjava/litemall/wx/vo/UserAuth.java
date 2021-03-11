package org.linlinjava.litemall.wx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    private String session_key;
    private String openid;
    private String anonymous_openid;
    private Integer errcode;
    private String errmsg;
}
