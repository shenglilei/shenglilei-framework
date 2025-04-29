package com.dofun.shenglilei.framework.common.tenant;

import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理租户信息
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/12/21
 * Time:10:16
 */
@Slf4j
public class TenantInfoHolder {
    /**
     * 保存当前租户信息
     * <p>
     * 作用域：当前线程，或子线程
     */
    private static final ThreadLocal<TenantInfo> HOLDER = new InheritableThreadLocal<>();

    /**
     * 返回当前租户ID
     */
    public Long getCurrentTenantId() {
        Long currentTenantId = HOLDER.get() == null ? null : HOLDER.get().getTenantId();
        log.debug("tenant id {}", currentTenantId);
        return currentTenantId;
    }

    /**
     * 设置当前租户ID
     */
    public void setCurrentTenantId(Long newTenantId) {
        if (newTenantId == null) {
            log.debug("newTenantId is null.");
            clearCurrentTenantId();
            return;
        }
        Long currentTenantId = getCurrentTenantId();
        if (currentTenantId != null && currentTenantId.equals(newTenantId)) {
            log.debug("tenant not change. id {}  -> {}", currentTenantId, newTenantId);
            return;
        }
        TenantInfo tenantInfo = new TenantInfo();
        tenantInfo.setTenantId(newTenantId);
        log.debug("tenant changed. id {}  -> {}", currentTenantId, newTenantId);
        if (currentTenantId != null) {
            clearCurrentTenantId();
        }
        HOLDER.set(tenantInfo);
    }

    /**
     * 设置当前租户ID
     */
    public void setCurrentTenantId(BaseRequestParam requestParam) {
        Long tenantId = requestParam == null ? null : requestParam.getCountryId() == null ? null : Long.valueOf(requestParam.getCountryId());
        setCurrentTenantId(tenantId);
    }

    /**
     * 设置当前租户ID
     */
    public void setCurrentTenantId(Integer tenantId) {
        setCurrentTenantId(Long.valueOf(tenantId));
    }

    /**
     * 清空租户ID
     */
    public void clearCurrentTenantId() {
        Long currentTenantId = getCurrentTenantId();
        if (currentTenantId != null) {
            log.debug("tenant clear. id {}  -> null", currentTenantId);
            HOLDER.remove();
        }
    }


    @Data
    public static class TenantInfo {
        private Long tenantId;
    }
}
