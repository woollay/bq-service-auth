package com.biuqu.boot.common.auth.model;

import com.biuqu.model.BaseBiz;
import com.biuqu.utils.IdUtil;
import lombok.Data;

/**
 * 二维码标准模型
 *
 * @author BiuQu
 * @date 2023/2/12 10:59
 */
@Data
public class QrCode extends BaseBiz<QrCodeResult>
{
    /**
     * 二维码
     */
    private String code;

    @Override
    public QrCodeResult toModel()
    {
        QrCodeResult qrResult = new QrCodeResult();
        qrResult.setOpenId(IdUtil.uuid());
        return qrResult;
    }
}
