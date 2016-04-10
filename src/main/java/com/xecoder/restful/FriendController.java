package com.xecoder.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.model.business.Friend;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.service.service.FriendService;
import com.xecoder.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
     * 查找清单
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    private ResponseEntity<?> search(@RequestParam(required = false) String name) {
        Friend friend = new Friend();
        if(StringUtils.isNotBlank(name))
            friend.setRemark(name);
        friend.setKeyUserId(this.getUserId());
        List<Friend> list = friendService.search(1,1000,null,friend);

        for(Friend f:list){
            User u = userServer.findById(f.getUser());
            if(u!=null){
                f.setAvatar(StringUtils.isBlank(u.getAvatar())?"":ImageUtil.getPathSmall(u.getAvatar()));
                f.setRemark(StringUtils.isBlank(f.getRemark())?u.getNickname():f.getRemark());
                f.setMotto(u.getMotto()!=null?u.getMotto():"");
            }
        }

        if(list.size()>0)
            return new ResponseEntity<>(list, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, NOT_FOUND);
    }


    /**
     * 添加好友
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}/{groupingName}", method = RequestMethod.POST)
    @ResponseBody
    private ResponseEntity<?> addFriend(@PathVariable String userId,@PathVariable String groupingName) {
        User user = userServer.findById(userId);
        if(user!=null) {
            Friend friend = new Friend();
            friend.setKeyUserId(this.getUserId());
            friend.setGrouping(groupingName);
            friend.setUser(userId);
            friend = friendService.addAdv(friend);

            Friend friend1 = new Friend();
            friend1.setKeyUserId(userId);
            friend1.setGrouping(groupingName);
            friend1.setUser(this.getUserId());
            friendService.addAdv(friend1);

            return new ResponseEntity<>(friend, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

    }

    /**
     * 修改好友备注
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "/remark/{id}/{name}", method = RequestMethod.POST)
    @ResponseBody
    private ResponseEntity<?> remark(@PathVariable String id,@PathVariable String name) {
        Friend friend = friendService.findById(id);
        if(friend!=null) {
            friend.setRemark(name);
            friendService.save(friend);

            return new ResponseEntity<>(friend, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

    }

    /**
     * 拉黑好友
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    private ResponseEntity<?> blackFriend(@PathVariable String userId) {
        User user = userServer.findById(userId);
        if(user!=null) {
            Date now = new Date();
            Friend friend = new Friend();
            friend.setKeyUserId(this.getUserId());
            friend.setUser(userId);
            List<Friend> list = friendService.search(1,10,null,friend);
            if(list!=null) {
                Friend friend2 = list.get(0);
                friend2.setBlacklist(true);
                friend2.setUpdateTime(now);
                friendService.save(friend2);//拉黑
                return new ResponseEntity<>(friend2, HttpStatus.OK);
            }
            else
                throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

    }
}
