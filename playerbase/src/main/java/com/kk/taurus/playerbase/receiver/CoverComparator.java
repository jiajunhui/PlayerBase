package com.kk.taurus.playerbase.receiver;

import java.util.Comparator;

public class CoverComparator implements Comparator<IReceiver> {
    @Override
    public int compare(IReceiver o1, IReceiver o2) {
        int x = 0;
        int y = 0;
        if(o1 instanceof BaseCover){
            x = ((BaseCover) o1).getCoverLevel();
        }
        if(o2 instanceof BaseCover){
            y = ((BaseCover) o2).getCoverLevel();
        }
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
