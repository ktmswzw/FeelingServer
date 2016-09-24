/**
 * Created by imanon.net on 16/9/4.
 */
var myIconAn,opts,myGeo,markerAnimation,map;
requirejs(['jquery','comm'],
    function () {
        requirejs(['async!http://api.map.baidu.com/api?v=2.0&ak=XE6mZVT9wt58O4uvzaUAZhNC&callback=init'],
            function () {
                map = new BMap.Map("allmap");
                var point = new BMap.Point(116.331398, 39.897445);
                map.centerAndZoom(point, 15);

                var geolocation = new BMap.Geolocation();
                geolocation.getCurrentPosition(function (r) {
                    if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                        var mk = new BMap.Marker(r.point);
                        $("#lat").val(r.latitude);
                        $("#lng").val(r.longitude);
                        map.addOverlay(mk);
                        map.panTo(r.point);
                        mk.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    }
                    else {
                        alert('failed' + this.getStatus());
                    }
                }, {enableHighAccuracy: true});

                opts = {
                    width: 250,     // 信息窗口宽度
                    height: 150,     // 信息窗口高度
                    title: "信息窗口", // 信息窗口标题
                    enableMessage: true//设置允许信息窗发送短息
                };



                $('#container').on('focus', '#to', function () {
                    var $weuiSearchBar = $('#search_bar');
                    $weuiSearchBar.addClass('weui_search_focusing');
                }).on('blur', '#to', function () {
                    var $weuiSearchBar = $('#search_bar');
                    $weuiSearchBar.removeClass('weui_search_focusing');
                    if ($(this).val()) {
                        $('#search_text').hide();
                    } else {
                        $('#search_text').show();
                    }
                }).on('touchend', '#submit', function () {
                    getPoint();
                }).on('touchend', '#search_clear', function () {
                    $('#to').val('');
                });


                $("#to").keydown(function(event){
                    if(event.keyCode == 13) {
                        getPoint();
                    }
                });

                function getPoint() {
                    var to = $("#to").val();
                    var lat = $("#lat").val();
                    var lng = $("#lng").val();
                    // if ($.trim(to) == "") {
                    //     doErrorMsg("输入查询为空", false);
                    //     return false;
                    // }
                    if ($.trim(lat) == ""||$.trim(lng) == "") {
                        doErrorMsg("等待定位", false);
                        return false;
                    }
                    var params = "lat="+lat+"&lng="+lng+"&to="+to;
                    $('#loadingToast').show();
                    setTimeout(function () {
                        $('#loadingToast').hide();
                    }, 1000);
                    $.ajax({
                        url: "http://192.168.43.17:8080/messages/searchWeb",
                        type: 'POST',
                        dataType: 'JSONP',
                        data: params,
                        jsonp: "callback",
                        jsonpCallback: "callback",
                        success: function (data) {
                            mapMove(data.pointList)
                        },
                        error: function (error) {
                            doErrorMsg("服务器离家出走，攻城狮在奋战", false);
                        }
                    });
                }
            });

    });


function mapMove(listPoint) {
    var key = 0, other=0;
    for (var i = 0; i < listPoint.length; i++) {
        var obj = listPoint[i];
        if(obj.type==0){
            other +=1;
        }
        else {
            key +=1;
        }
        var tempPoint = new BMap.Point(obj.point.x, obj.point.y);
        goto(tempPoint, obj);
    }

    setTimeout(function () {
        doErrorMsg("小主，找到"+key+"个,周边"+other+"个", true);
    }, 500);

}

function goto(point, obj) {
    //map.centerAndZoom(point, 7);
    setTimeout(function(){
        var convertor = new BMap.Convertor();
        var pointArr = [];
        pointArr.push(point);
        convertor.translate(pointArr, 1, 5, translateCallback)
    }, 10);

    //坐标转换完之后的回调函数
    translateCallback = function (data){
        if(data.status === 0) {
            // var img = obj.avatar.replace("small","superSmall");
            var img = obj.avatar;
            myIconAn = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                offset: new BMap.Size(10, 25), // 指定定位位置
                imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
            });
            // myIconAn = new BMap.Icon(img, new BMap.Size(60, 60));
            var markergps = new BMap.Marker(data.points[0], {icon: myIconAn});
            map.setCenter(data.points[0]);
            var content = '<div style="margin:0;line-height:20px;padding:2px;">' +
                '<img src="'+obj.avatar+'"  style="float:left;zoom:1;overflow:hidden;width:100px;height:100px;margin:10px;"/>';
            content += '<br>来自['+ obj.from + "]";
            if(obj.question!=null&&obj.question!=""){
                content+="<br>问题:"+obj.question;
            }
            content+='<br><a href="javascript:;" class="weui_btn weui_btn_mini weui_btn_default">去下载查看</a> </div>';
            map.addOverlay(markergps);               // 将标注添加到地图中
            addClickHandler(content, markergps, obj.title);

        }
    }

}

function addClickHandler(content, marker, title) {
    marker.addEventListener("click", function (e) {
            openInfo(content, e, title)
        }
    );
}

function openInfo(content, e, title) {
    var p = e.target;
    var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
    opts.title = title;
    var infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象
    map.openInfoWindow(infoWindow, point); //开启信息窗口
}