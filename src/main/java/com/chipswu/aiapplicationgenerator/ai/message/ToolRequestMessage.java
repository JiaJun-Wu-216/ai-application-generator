package com.chipswu.aiapplicationgenerator.ai.message;

import dev.langchain4j.model.chat.response.PartialToolCall;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工具调用消息
 *
 * @author WuJiaJun
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolRequestMessage extends StreamMessage {

    private String id;

    private String name;

    private String arguments;

    public ToolRequestMessage(PartialToolCall PartialToolCall) {
        super(StreamMessageTypeEnum.TOOL_REQUEST.getValue());
        this.id = PartialToolCall.id();
        this.name = PartialToolCall.name();
        this.arguments = PartialToolCall.partialArguments();
    }
}
