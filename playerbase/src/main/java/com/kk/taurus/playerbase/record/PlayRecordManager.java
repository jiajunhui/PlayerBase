package com.kk.taurus.playerbase.record;

import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/12/12.
 *
 * Play Record Manager for user use it.
 *
 */
public class PlayRecordManager {

    private static final int DEFAULT_MAX_RECORD_COUNT = 200;

    static int MaxRecordCount = DEFAULT_MAX_RECORD_COUNT;

    private static RecordKeyProvider recordKeyProvider;

    public static void setRecordConfig(RecordConfig recordConfig){
        if(recordConfig==null)
            recordConfig = new RecordConfig.Builder()
                    .setMaxRecordCount(DEFAULT_MAX_RECORD_COUNT)
                    .setRecordKeyProvider(new DefaultRecordKeyProvider()).build();
        MaxRecordCount = recordConfig.getMaxRecordCount();
        recordKeyProvider = recordConfig.getRecordKeyProvider();
    }

    public static RecordKeyProvider getRecordKeyProvider(){
        if(recordKeyProvider==null)
            return new DefaultRecordKeyProvider();
        return recordKeyProvider;
    }

    public static int removeRecord(DataSource dataSource){
        return PlayRecord.get().removeRecord(getRecordKeyProvider().generatorKey(dataSource));
    }

    public static void clearRecord(){
        PlayRecord.get().clearRecord();
    }

    public static void destroyCache(){
        PlayRecord.get().destroy();
    }

    public static class RecordConfig{

        private int maxRecordCount;
        private RecordKeyProvider recordKeyProvider;

        RecordConfig(int maxRecordCount, RecordKeyProvider recordKeyProvider) {
            this.maxRecordCount = maxRecordCount;
            this.recordKeyProvider = recordKeyProvider;
        }

        public int getMaxRecordCount() {
            return maxRecordCount;
        }

        public RecordKeyProvider getRecordKeyProvider() {
            return recordKeyProvider;
        }

        public static class Builder{

            private int maxRecordCount;
            private RecordKeyProvider recordKeyProvider;

            public int getMaxRecordCount() {
                return maxRecordCount;
            }

            public Builder setMaxRecordCount(int maxRecordCount) {
                this.maxRecordCount = maxRecordCount;
                return this;
            }

            public RecordKeyProvider getRecordKeyProvider() {
                return recordKeyProvider;
            }

            public Builder setRecordKeyProvider(RecordKeyProvider recordKeyProvider) {
                this.recordKeyProvider = recordKeyProvider;
                return this;
            }

            public RecordConfig build(){
                return new RecordConfig(maxRecordCount, recordKeyProvider);
            }

        }

    }

}
