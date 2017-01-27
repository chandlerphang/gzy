package com.cactus.guozy.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ErrorWrapper extends BaseWrapper {

    @XmlElement
    protected Integer errno;

    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    protected List<ErrorMsgWrapper> messages;

    public Integer getErrno() {
        return this.errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public List<ErrorMsgWrapper> getMessages() {
        if (this.messages == null) {
            this.messages = new ArrayList<ErrorMsgWrapper>();
        }
        return this.messages;
    }

    public void setMessages(List<ErrorMsgWrapper> messages) {
        this.messages = messages;
    }

}
