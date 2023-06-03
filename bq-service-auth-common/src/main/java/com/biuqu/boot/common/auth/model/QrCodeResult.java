package com.biuqu.boot.common.auth.model;

import lombok.Data;

/**
 * 扫码后的结果
 *
 * @author BiuQu
 * @date 2023/2/12 11:00
 */
@Data
public class QrCodeResult
{
    /**
     * 扫码成功后的唯一标识
     */
    private String openId;
}
