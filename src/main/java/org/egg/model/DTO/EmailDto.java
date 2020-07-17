package org.egg.model.DTO;

import java.io.Serializable;

/**
 * @author cdt
 * @Description
 * @date: 2018/8/10 20:24
 */
public class EmailDto implements Serializable {
    private String title;
    private String content;
    private String toEmailAcc;

    public EmailDto(String title, String content, String toEmailAcc) {
        this.title = title;
        this.content = content;
        this.toEmailAcc = toEmailAcc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToEmailAcc() {
        return toEmailAcc;
    }

    public void setToEmailAcc(String toEmailAcc) {
        this.toEmailAcc = toEmailAcc;
    }
}
