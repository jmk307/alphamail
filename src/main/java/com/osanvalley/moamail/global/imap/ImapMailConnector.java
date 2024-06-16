package com.osanvalley.moamail.global.imap;

import javax.mail.*;

public interface ImapMailConnector {
    public Folder connect() throws MessagingException;
    public void disconnect() throws MessagingException;
    public Message[] getMessages() throws MessagingException;
}
