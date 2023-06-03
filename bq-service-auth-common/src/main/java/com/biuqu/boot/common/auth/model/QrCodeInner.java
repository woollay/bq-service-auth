package com.biuqu.boot.common.auth.model;

import com.biuqu.model.BaseBizInner;
import lombok.Data;

/**
 * 二维码入参模型
 *
 * @author BiuQu
 * @date 2023/2/12 10:58
 */
@Data
public class QrCodeInner extends BaseBizInner<QrCode>
{
    @Override
    protected QrCode genModel()
    {
        QrCode qrCode = new QrCode();
        qrCode.setCode(code);
        return qrCode;
    }

    /**
     * 扫码时的code
     */
    private String code;
}
