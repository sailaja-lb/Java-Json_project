package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONValidate {


    @JsonProperty
    String object_or_array;

    @JsonProperty
    Boolean empty;

    @JsonProperty
    Long parse_time_nanoseconds;

    @JsonProperty
    Boolean validate;

    @JsonProperty
    Integer size;

    @JsonProperty
    String error;

    @JsonProperty
    String error_info;

    public JSONValidate(String input) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            long parse_time_ns_start = System.nanoTime();
            JsonNode jsonNode = objectMapper.readTree(input);
            long parse_time_ns_end = System.nanoTime();
            String objectOrArray = null;

            if(jsonNode.isObject()) {
                objectOrArray = "object";
            } else if (jsonNode.isArray()) {
                objectOrArray = "array";
            }
            this.object_or_array = objectOrArray;
            this.validate = true;
            this.size = jsonNode.size();
            this.empty = this.size > 0 ? false : true;
            this.parse_time_nanoseconds = parse_time_ns_end - parse_time_ns_start;
        } catch (JsonProcessingException e) {
            this.error = e.getMessage();
            this.validate = false;
            this.error_info = "This error came from JsonProject";
        }

    }
    }


