package com.osanvalley.moamail.domain.mail.util;

import javax.mail.*;

public interface ImapMailConnector {
    public Folder connect() throws MessagingException;
    public void disconnect() throws MessagingException;
    public Message[] getMessages() throws MessagingException;
}
