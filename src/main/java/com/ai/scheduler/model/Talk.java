
package com.ai.scheduler.model;

import com.ai.scheduler.util.Utils;
import lombok.Data;

import java.util.List;

@Data
public class Talk implements Comparable<Talk> {

    private String description;
    private List<String> tags;
    private String title;
    private TalkType type;

    @Override
    public int compareTo(Talk o) {
        return Utils.getDuration(o) - Utils.getDuration(this);
    }
}
