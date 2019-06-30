
package com.ai.scheduler.model;

import com.ai.scheduler.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
