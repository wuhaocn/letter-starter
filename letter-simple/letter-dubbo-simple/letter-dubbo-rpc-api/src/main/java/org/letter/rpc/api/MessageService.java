package org.letter.rpc.api;

public interface MessageService {
	MessageResponse send(MessageRequest messageRequest);
}
