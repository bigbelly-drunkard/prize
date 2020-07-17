package org.egg.model.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by chendatao on 2017/12/4.
 */
@Data
public class RegisterDto {
    @NotBlank
    private String loginName;
    @NotBlank
    private String pwd;
    @NotBlank
    private String verificationCode;

    private String recommendNo;


}
