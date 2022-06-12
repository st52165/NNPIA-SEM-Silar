package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response;

import lombok.Data;

@Data
public class ResponseMessage {

    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }
}
