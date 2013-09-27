/*
 * Copyright (c) 2007-2013 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.plugin.mongocollab;

import com.mongodb.ReflectionDBObject;
import org.broad.igv.feature.AbstractFeature;
import org.broad.igv.feature.BasicFeature;
import org.broad.igv.session.SubtlyImportant;
import org.broad.tribble.Feature;

/**
 * Object mapping to Mongo database
 * ReflectionDBObject works with getters/setters, and
 * doesn't use the Java Beans case convention.
 * So (get/set)Chr maps to a field named "Chr", not "chr"
 * as we might prefer
 *
 * TODO Use existing feature interfaces/classes, which are long past
 * TODO overdue for refactoring
 */
public class DBFeature extends ReflectionDBObject implements Feature {

    private String chr;
    private int start;
    private int end;
    private String description;
    private double score;

    private String name;

    @SubtlyImportant
    public DBFeature(){}

    public DBFeature(String chr, int start, int end, String description, double score, String name){
        this.chr = chr;
        this.start = start;
        this.end = end;
        this.description = description;
        this.score = score;
        this.name = name;
    }

    static DBFeature create(Feature feature){
        if(feature instanceof AbstractFeature){
            return create((AbstractFeature) feature);
        }
        return new DBFeature(feature.getChr(), feature.getStart(), feature.getEnd(), null, 0, null);
    }

    static DBFeature create(AbstractFeature feature){
        return new DBFeature(feature.getChr(), feature.getStart(), feature.getEnd(), feature.getDescription(), feature.getScore(), feature.getName());
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public IGVFeat createIGVFeature(){
        return new IGVFeat(this);
    }

    /**
     * This is the feature to be returned to {@code FeatureTrack}s.
     * Would really like to inherit both ReflectionDBObject and BasicFeature, but
     * that's not possible.
     */
    public static class IGVFeat extends BasicFeature{

        private DBFeature dbFeat;

        IGVFeat(DBFeature dbFeat){
            super(dbFeat.getChr(), dbFeat.getStart(), dbFeat.getEnd());
            this.dbFeat = dbFeat;
            setName(dbFeat.getName());
            setDescription(dbFeat.getDescription());
            //TODO Shouldn't just cast from double to float
            setScore((float) dbFeat.getScore());
        }

        DBFeature getDBFeature(){
            return this.dbFeat;
        }
    }
}