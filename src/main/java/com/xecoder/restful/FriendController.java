package com.xecoder.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.model.business.Friend;
import com.xecoder.model.business.User;
import com.xecoder.service.service.FriendService;
import com.xecoder.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by vincent on 4/6/16.
 */
@RestController
@RequestMapping("/friend")
public class FriendController extends BaseController {
    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userServer;


    /**
     * 添加好友
     * @param userId
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    private ResponseEntity<?> addFriend(@RequestParam String userId,@RequestParam(required = false) String groupingName) {
        User user = userServer.findById(userId);
        if(user!=null) {
            Date now = new Date();
            Friend friend = new Friend();
            friend.setKeyUserId(this.getUserId());
            friend.setGrouping(groupingName);
            friend.setUser(userId);
            friend.setCreateTime(now);
            friend.setUpdateTime(now);
            friendService.addAdv(friend);
            return new ResponseEntity<>(friend, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

    }
}
