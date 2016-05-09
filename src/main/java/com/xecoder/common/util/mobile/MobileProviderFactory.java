package com.xecoder.common.util.mobile;

import com.xecoder.common.util.mobile.provider.ChinaMobileProvider;
import com.xecoder.common.util.mobile.provider.ChinaTelecomProvider;
import com.xecoder.common.util.mobile.provider.ChinaUnicomProvider;
import com.xecoder.common.util.mobile.provider.MobileProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanglu
 */
public class MobileProviderFactory {

    static List<MobileProvider> providers = new ArrayList<MobileProvider>();

    static {
        providers.add(new ChinaMobileProvider());
        providers.add(new ChinaTelecomProvider());
        providers.add(new ChinaUnicomProvider());
    }

    static MobileProviderFactory instance = new MobileProviderFactory();

    public static MobileProviderFactory getInstance() {
        return instance;
    }

    public List<MobileProvider> getMobileProviders() {
        return providers;
    }

    public Boolean isValidPhone(long phone) {
        for (MobileProvider provider : getMobileProviders()) {
            if (provider.isValidPhone(phone)){
                return true;
            }
        }
        return false;
    }

}
