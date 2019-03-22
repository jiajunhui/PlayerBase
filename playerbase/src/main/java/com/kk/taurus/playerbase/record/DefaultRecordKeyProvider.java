package com.kk.taurus.playerbase.record;

import android.net.Uri;
import android.text.TextUtils;

import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/12/12.
 */
public class DefaultRecordKeyProvider implements RecordKeyProvider {

    @Override
    public String generatorKey(DataSource dataSource) {
        String data = dataSource.getData();
        Uri uri = dataSource.getUri();
        String assetsPath = dataSource.getAssetsPath();
        int rawId = dataSource.getRawId();
        if(!TextUtils.isEmpty(data)){
            return data;
        }else if(uri!=null){
            return uri.toString();
        }else if(!TextUtils.isEmpty(assetsPath)){
            return assetsPath;
        }else if(rawId > 0){
            return String.valueOf(rawId);
        }
        return dataSource.toString();
    }

}
