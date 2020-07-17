package org.egg.model.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by chendatao on 2017/12/7.
 */
@Data
public class LoginDto implements Serializable {
    @NotBlank
    private String loginName;
    @NotBlank
    private String pwd;
    private String wxMiniOpenId;
    private String wxOpenId;
}
