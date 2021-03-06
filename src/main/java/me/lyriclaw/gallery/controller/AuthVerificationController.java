package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.vo.ApiResp;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "Authentication Verification APIs")
@Validated
@RestController
@RequestMapping("/api/private/verification")
public class AuthVerificationController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation("Check if auth success")
    ApiResp<Object> check() {
        return ApiResp.success();
    }

}
