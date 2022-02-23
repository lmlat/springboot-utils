(function (window) {
    /**
     * 自定义 $ 选择器，获取DOM对象
     * @param selector 选择器
     * @returns {_$_} 返回DOM对象
     */
    window.$ = window.JS = function (selector) {
        return new _$_(selector);
    };


    /**
     * 定义私有化构造方法，用来实现链式操作（选择器的实现）
     * @param selector 选择器
     * @returns {_$_} 返回DOM对象
     * @private 私有化构造器
     */
    _$_ = function (selector) {
        if (typeof selector == 'string' || selector instanceof String) {
            //获取DOM对象
            //校验当前浏览器是否支持querySelector
            if (document.querySelectorAll) {
                let nodeList = document.querySelectorAll(selector);
                this.el = nodeList != null && nodeList.length === 1 ? nodeList[0] : nodeList;
            }
        } else {
            //处理原生对象的情况, $(null), $(undefined), $(false), $(function(){})等
            this.el = selector;
        }
    };

    /**
     * 对外暴露的方法
     */
    _$_.prototype = {
        /**
         * 构造方法
         */
        constructor: _$_,
        html: _html_,
        text: _text_,
        val: _val_,
        attr: _attr_,
        removeAttr: _removeAttr_,
        addClass: _addClass_,
        removeClass: _removeClass_,
        toggleClass: _toggleClass_,
        siblings: _siblings_,
        bind: _bindEvent_,
        classList: { //与内置classList功能类似
            contains: _hasClass_,
            addClass: _insertClass_,
            removeClass: _deleteClass_,
            replaceClass: _replaceClass_
        }
    }

    /**
     * 公共工具对象
     */
    window.CommonUtils = {
        /**
         * 生成唯一ID = 自定义prefix前缀 + 时间戳10进制前一半 + 随机数36进制 + 时间戳10进制后一半 + 时间戳36进制
         * @param prefix 自定义前缀
         * @returns {string} 唯一ID序列
         */
        generateId: function (prefix) {
            prefix = prefix || "ID_";
            let time = Date.now(),
                now10Radix = time.toString(),
                now36Radix = time.toString(36),
                mid = time.length >> 1;
            //前缀 + 时间戳10进制前一半 + 随机数36进制 + 时间戳10进制后一半 + 时间戳36进制
            return (prefix + now10Radix.substring(0, mid) + Math.random().toString(36).substring(2, this.randomRange(1, 10)) + now10Radix.substring(mid) + now36Radix).toUpperCase();
        },
        /**
         * 检测当前操作的设备环境是否为电脑端
         * @returns {boolean} true or false
         */
        isIOS: function () {
            let userAgent = navigator.userAgent;
            if (userAgent.indexOf('Android') > -1 || userAgent.indexOf('Linux') > -1) {//安卓手机
                return false;
            } else if (userAgent.indexOf('iPhone') > -1) {//苹果手机
                return true;
            } else if (userAgent.indexOf('iPad') > -1) {//iPad
                return false;
            } else if (userAgent.indexOf('Windows Phone') > -1) {//Windows Phone手机
                return false;
            } else {
                return false;
            }
        },
        /**
         * 检测当前操作的设备环境是否为电脑端
         * 手机端口User-Agent大全
         * (1) https://blog.csdn.net/he_ranly/article/details/88907820
         * (2) https://blog.csdn.net/rztyfx/article/details/7920841
         * (3) https://www.cnblogs.com/xiamidong/p/4092408.html
         * @returns {boolean} true or false
         */
        isPc: function () {
            let userAgent = navigator.userAgent,
                agentList = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
            for (const agent of agentList) {
                if (userAgent.indexOf(agent) > 0) {
                    return false;
                }
            }
            return true;
        },
        /**
         * 当前是否处于微信环境
         * @returns {boolean} true or false
         */
        isWechatWeb: function () {
            return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1;
        },
        /**
         * 当前是否处于小程序环境
         * @returns {Promise<boolean>} true or false
         */
        isWechatApplet: function () {
            return new Promise(resolve => {
                if (!this.isWechatWeb()) {
                    //不在微信或者小程序中
                    resolve(false);
                } else {
                    wx.miniProgram.getEnv(res => {
                        if (res.miniprogram) {
                            resolve(true);//在小程序中
                        } else {
                            resolve(false);
                        }
                    })
                }
            })
        },
        /**
         * 获取当前操作的浏览器类型
         * @returns {string} 类型名
         */
        getBrowserType: function () {
            return this.getBrowserInfo()[1];
        },
        /**
         * 获取浏览器信息，
         * @returns {[]|[[]]} 返回值包含3个值, 第1个:操作系统类型,第2个:浏览器类型,第3个:地址编号
         */
        getBrowserInfo: function () {
            //获取浏览器User-Agent值
            //谷歌的User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36
            //火狐的User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0
            return _resolveUserAgentInfo_(StringUtils.toLower(navigator.userAgent), ["trident", "edge", "firefox", "opr", "safari", "chrome"], [Regexps.RG_TRIDENT_BROWSER_STANDARD, Regexps.RG_EDGE_BROWSER_STANDARD, Regexps.RG_FIREFOX_BROWSER_STANDARD, Regexps.RG_OPERA_BROWSER_STANDARD, Regexps.RG_SAFARI_BROWSER_STANDARD, Regexps.RG_CHROME_BROWSER_STANDARD]);
        },
        /**
         * 获取设备号，安卓，ios，web
         * @returns {string} 当前操作的设备类型, web,ios,android等
         */
        getDeviceType: function () {
            //获取user-agent信息
            let userAgent = navigator.userAgent
            //检测当前操作设备是否为android类型
            let isAndroid = userAgent.indexOf('Android') > -1 || userAgent.indexOf('Adr') > -1
            //检测当前操作设备是否为ios类型
            let isIOS = userAgent.match(Regexps.RG_IOS_DEVICE_TYPE_STANDARD);
            return isAndroid ? "ANDROID" : isIOS ? "IOS" : "WEB";
        },
        /**
         * 滚动到具体位置
         * @param number 滚动距离
         * @param time 延迟时间
         */
        scrollTop: function (number = 0, time) {
            if (!time) {
                document.body.scrollTop = document.documentElement.scrollTop = number;
                return number;
            }
            const spacingTime = 20;
            let spacingIndex = time / spacingTime;
            let nowTop = document.body.scrollTop + document.documentElement.scrollTop;
            let everTop = (number - nowTop) / spacingIndex;
            let scrollTimer = setInterval(() => {
                if (spacingIndex-- > 0) {
                    scrollTop(nowTop += everTop);
                } else {
                    clearInterval(scrollTimer);
                }
            }, spacingTime);
        }
        ,
        /**
         * 随机生成一个指定范围[min,max]之间的数
         * @param min 左区间
         * @param max 右区间
         * @returns {number} 返回一个随机数
         */
        randomRange: (min, max) => parseInt(Math.random() * (max - min + 1) + min)//5 10, [0*6+5,1*6+5)
    }
    /**
     * 常用正则表达式
     */
    const Regexps = {
        //表示匹配连续的多个相同的任意字符，只要找到字符串里面存在连续的两个或者以上的相同字符即匹配。
        //\\1表示取第一个括号匹配的内容，后面的加号表示匹配1次或多次，加在一起就是某个字符重复两次或多次就匹配
        RG_REMOVE_DUPLICATE: /([\w\u2E80-\u9FFF])\1+"/,//字符串去重
        RG_IMG_URL: /<img[^>]+src=['"]([^'"]+)['"]+/, //匹配img标签中的src属性
        RG_VERSION_NUMBER: /^(\d)+(\.\d+)+$/, //匹配版本号XX.XX.XX
        RG_LEFT_TRIM: /(^\s*)/, //清除首部空白字符
        RG_RIGHT_TRIM: /(\s*$)/, //清除尾部空白字符
        RG_LEFT_RIGHT_TRIM: /(^\s*)|(\s*$)/,//清除首尾空白字符
        RG_ALL_TRIM: /(^\s*)|(\s*$)|(\s*|\t|\r|\n)/,//清除首尾空白字符(包含内容中的留白)
        RG_EMAIL_STANDARD: /^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/,//匹配邮箱
        RG_PHONE_STANDARD: /^13[0-9]{9}|15[012356789][0-9]{8}|18[0-9]{9}|(14[57][0-9]{8})|(17[015678][0-9]{8})$/,//匹配手机号
        RG_USERNAME_STANDARD: /^[a-zA-Z\u2E80-\u9FFF]{2,15}$/, //匹配账户名
        RG_PASSWORD_STANDARD: /^[a-zA-z]\w{6,16}$/, //账户密码,以字母开头
        RG_PASSWORD_STRONG_STANDARD: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,16}$/,//强密码，必须包含大小写字母和数字的组合，不能使用特殊字符，长度在6-16之间
        RG_CHINESE_STANDARD: /^[\u2E80-\u9FFF]+$/, //匹配汉字
        RG_VALID_CODE_STANDARD: /^[a-zA-Z0-9]{5}$/, //匹配验证码
        RG_NUMBER_STANDARD: /^([+-]?)\d*\.?\d+$/, //匹配正负数，符号位(0个或1个) + 数字(1个或多个) + 小数点(0个或1个) + 数字(1个或多个)
        RG_POSITIVE_INTEGER_STANDARD: /^[1-9]\d*$/, //匹配正整数
        RG_NEGTIVE_INTEGER_STANDARD: /^([-]?)[1-9]\d*$/,//匹配负整数
        RG_FLOAT_NUMBER_STANDARD: /^([+-]?)\d*\.?\d+$/,//匹配浮点数
        RG_LETTER_STANDARD: /^[a-zA-Z]+$/,//匹配字母
        RG_UPPERCASE_LETTER_STANDARD: /^[A-Z]+$/, //匹配大写字母
        RG_LOWERCASE_LETTER_STANDARD: /^[a-z]+$/, //匹配小写字母
        RG_POSTAL_CODE_STANDARD: /^\d{6}$/, //匹配邮政编码
        RG_IPV4_STANDARD: /^(25[0-5]|2[0-4]\\d|1\d{2}|\d{1,2})(\.(25[0-5]|2[0-4]\d|1\d{2}|\d{1,2})){3}$/, //匹配IPv4地址
        RG_IMG_STANDARD: /(.*)\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$/,//匹配图片地址
        RG_DATE_STANDARD: /^\d{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$/, //匹配日期(yyyy-MM-dd)|(yyyy/MM/dd)|(yyyy年MM月dd日)
        RG_RAR_FILE_STANDARD: /(.*)\.(rar|zip|7zip|tgz)$/,//匹配压缩文件
        RG_QQ_NUMBER_STANDARD: /^[1-9]\d{4,14}$/, //匹配QQ号码，最短5位，最长15位数字
        RG_CAR_NUMBER_STANDARD: /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/, //匹配车牌号
        RG_IOS_DEVICE_TYPE_STANDARD: /\(i[^;]+;( U;)? CPU.+Mac OS X/, //匹配IOS设备型号
        RG_ID_CARD_STANDARD: /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/,
        RG_DIGIT_CHARACTER: /\d/, //匹配数字
        RG_DIGIT_STANDARD: /\d/g, //匹配数字
        RG_UPPER_CHARACTER: /[A-Z]/,//匹配大写字符
        RG_LOWER_CHARACTER: /[a-z]/,//匹配小写字符
        RG_SPECIAL_CHARACTER: /[!@#$%^&*()_\-+={\[\]\{}|\\:;"'?/>\.<,！￥（）—【】‘’；：？、》《。，]/, //匹配特殊字符
        RG_DATE_FORMAT_STANDARD: /{(y|m|d|h|M|s|w)+}/g, //格式化日期标准
        RG_EDGE_BROWSER_STANDARD: /edge\/[\d.]+/gi, //匹配edge类型浏览器
        RG_TRIDENT_BROWSER_STANDARD: /trident\/[\d.]+/gi, //匹配IE类型浏览器
        RG_FIREFOX_BROWSER_STANDARD: /firefox\/[\d.]+/gi, //匹配firefox类型浏览器
        RG_CHROME_BROWSER_STANDARD: /chrome\/[\d.]+/gi, //匹配chrome类型浏览器
        RG_SAFARI_BROWSER_STANDARD: /safari\/[\d.]+/gi, //匹配safari类型浏览器
        RG_OPERA_BROWSER_STANDARD: /opr\/[\d.]+/gi, //匹配opera_类型浏览器
    };
    /**
     * 正则校验工具对象
     */
    window.RegexUtils = {
        /**
         * 获取指定content中所有指定tagName的指定attrName值
         *
         * @param content 内容
         * @param tagName 标签名
         * @param attrName 属性名
         * @returns {[]} 返回匹配的所有属性值（顺序存储）
         */
        getLabelAttr: function (content, tagName = 'img', attrName = 'src') {
            let result = [], temp;
            //拼接动态正则表达式
            let rg = new RegExp("<" + tagName + "[^>]+" + attrName + "=['\"]([^'\"]+)['\"]+", "ig")
            while ((temp = rg.exec(content)) != null) {
                result.push(temp[1]);
            }
            return result;
        },

        /**
         * 匹配指定的标签数量
         *
         * @param content 匹配内容
         * @param tagName 匹配标签名
         * @returns {number} 返回匹配到的标签数量
         */
        getLabelCount: function (content, tagName = 'img') {
            //拼接动态正则表达式
            let matchContentArray = content.match(new RegExp("<" + tagName + ".*?>", "ig"));
            return matchContentArray != null ? matchContentArray.length : 0;
        },

        /**
         * 校验身份证号
         *
         * @param idCard 身份证号
         * @returns {boolean} true or false
         */
        isIdCard: function (idCard) {
            if (Regexps.RG_ID_CARD_STANDARD.test(idCard)) {//如果通过该验证，说明身份证格式正确，但准确性还需计算
                if (idCard.length === 18) {
                    let idCardWi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]  //将前17位加权因子保存在数组里
                    let idCardY = [1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2]//这是除以11后，可能产生的11位余数、验证码，也保存成数组
                    let idCardWiSum = 0 //用来保存前17位各自乖以加权因子后的总和
                    for (let i = 0; i < 17; i++) {
                        idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i]
                    }
                    let idCardMod = idCardWiSum % 11 //计算出校验码所在数组的位置
                    let idCardLast = idCard.substring(17) //得到最后一位身份证号码
                    //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
                    if (idCardMod === 2) {
                        return idCardLast === 'X' || idCardLast === 'x';
                    } else {
                        //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                        return idCardLast === idCardY[idCardMod];
                    }
                }
            }
            return false;
        },

        /**
         * 身份证打码
         *
         * @param idCard 身份证号
         * @returns {string} 返回打码后的身份证号
         */
        idCardMask: function (idCard) {
            if (StringUtils.isBlank(idCard) || !this.isIdCard(idCard)) {
                alert("请输入正确的身份证号");
                return idCard;
            }
            return idCard.substr(0, 1) + idCard.slice(1, -4).replace(Regexps.RG_DIGIT_STANDARD, '*') + idCard.substr(-4)
        },

        /**
         * 检测密码强度
         *
         * @param psd 密码
         * @returns {number} 返回密码级别
         */
        checkPassword: function (psd) {
            let level = 0;
            if (psd.length < 6) return level; // 密码级别不够
            if (Regexps.RG_DIGIT_CHARACTER.test(psd)) level++;
            if (Regexps.RG_LOWER_CHARACTER.test(psd)) level++;
            if (Regexps.RG_UPPER_CHARACTER.test(psd)) level++;
            if (Regexps.RG_SPECIAL_CHARACTER.test(psd)) level++;
            return level;
        },

        /**
         * 正则校验
         *
         * @param str 匹配字符串
         * @param type 匹配正则类型
         * @returns {boolean|*} true or false
         */
        check(str, type) {
            switch (StringUtils.toLower(type)) {
                case 'phone'://手机号
                    return Regexps.RG_PHONE_STANDARD.test(str);
                case 'tel'://座机
                    return /^(0\d{2,3}-\d{7,8})(-\d{1,4})?$/.test(str);
                case 'idcard'://身份证
                    return this.isIdCard(str);
                case 'password'://密码以字母开头，长度在6~18之间，只能包含字母、数字和下划线
                    return Regexps.RG_PASSWORD_STANDARD.test(str)
                case 'postal'://邮政编码
                    return Regexps.RG_POSTAL_CODE_STANDARD.test(str);
                case 'qq'://QQ号
                    return Regexps.RG_QQ_NUMBER_STANDARD.test(str);
                case 'email'://邮箱
                    return Regexps.RG_EMAIL_STANDARD.test(str);
                case 'url'://网址
                    return /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/.test(str)
                case 'ip'://IP
                    return /((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))/.test(str);
                case 'date'://日期时间
                    return /^(\d{4})\-(\d{2})\-(\d{2}) (\d{2})(?:\:\d{2}|:(\d{2}):(\d{2}))$/.test(str) || /^(\d{4})\-(\d{2})\-(\d{2})$/.test(str)
                case 'number'://数字
                    return Regexps.RG_DIGIT_CHARACTER.test(str);
                case 'english'://英文
                    return Regexps.RG_LETTER_STANDARD.test(str);
                case 'chinese'://中文
                    return Regexps.RG_CHINESE_STANDARD.test(str);
                case 'lower'://小写
                    return Regexps.RG_LOWERCASE_LETTER_STANDARD.test(str);
                case 'upper'://大写
                    return Regexps.RG_UPPERCASE_LETTER_STANDARD.test(str);
                default:
                    return true;
            }
        }
    }

    /**
     * 数组工具对象
     */
    window.ArrayUtils = {
        /**
         * 判断一个元素是否存在数组中
         *
         * @param arr 数组
         * @param val 值
         * @returns {boolean} true or false
         */
        contains: function (arr, val) {
            return arr.indexOf(val) !== -1;
        },
        /**
         * 二分查询,前置条件,数组必须有序
         *
         * @param arr 数组
         * @param val 查找的值
         * @returns {number} 若val值存在数组中,则返回指定的下标,否则返回-1
         */
        binarySearch: function (arr, val) {
            let left = 0, right = arr.length - 1, mid;
            while (left <= right) {
                mid = left + ((right - left) >> 1);
                if (arr[mid] < val) {
                    left = mid + 1;
                } else if (arr[mid] > val) {
                    right = mid - 1;
                } else {
                    return mid;
                }
            }
            return -1;
        },
        /**
         * foreach操作
         *
         * @param arr 数组
         * @param fn 回调函数
         */
        each: function (arr, fn) {
            fn = fn || Function;
            //获取回调参数fn, arguments是形参列表数组, argument[1]表示获取第2个参数,
            let callback = Array.prototype.slice.call(arguments, 1);
            for (let i = 0; i < arr.length; i++) {
                //[arr[i], i].concat(callback)理解
                //对应回调函数的参数列表, param1: 元素值, param2: 元素下标, param3: 回调函数
                //执行回调函数
                //相当于执行了function(ele, index, function(){});这个函数,把当前的参数值传递了过去而已
                fn.apply(arr, [arr[i], i].concat(callback));
            }
        },
        /**
         * 对数组中每个元素进行一次操作(与JS内置数组中的map函数功能式样)
         *
         * @param arr 数组
         * @param fn 回调函数
         * @param thisObj 当前对象
         * @returns {*[]} 返回处理后的数组
         */
        map: function (arr, fn, thisObj) {
            //当前作用域, this或window
            let scope = thisObj || window;
            //保存每次回调函数处理过的原数组的每个元素
            let temp = [];
            for (let i = 0, j = arr.length; i < j; ++i) {
                let res = fn.call(scope, arr[i], i, this);
                if (res != null) {
                    temp.push(res);
                }
            }
            return temp;
        },

        /**
         * 排序
         *
         * @param arr 数组
         * @param comparator 排序器, 1升序,2降序,3错排序
         * @returns {*} 返回处理后的数组
         */
        sort: (arr, comparator = 1) => arr.sort((a, b) => {
            switch (comparator) {
                case 1:
                    return a - b;
                case 2:
                    return b - a;
                case 3:
                    return Math.random() - 0.5; //打乱数组中的元素
                default:
                    return arr;
            }
        }),

        /**
         * 去重
         *
         * @param arr 数组
         * @returns {[]} 返回处理后的数组
         */
        unique: function (arr) {
            if (Array.hasOwnProperty('from')) {
                return Array.from(new Set(arr)); //通过Set去重
            } else { //通过遍历数组标记元素进行去重
                let bool = {}, result = [];
                for (let ele of arr) {
                    if (!bool[ele]) {
                        bool[ele] = true;//添加过的元素标记为true
                        result.push(ele);
                    }
                }
                return result;
            }
        },

        /**
         * 去重
         * ...rest运算符放弃所有重复的值
         *
         * @param arr 数组
         * @returns {[]} 返回处理后的数组
         */
        distinct: arr => [...new Set(arr)],

        /**
         * 求两个集合的并集
         *
         * @param a 数组a
         * @param b 数组b
         * @returns {*[]} 返回处理后的数组
         */
        //合并两个数组的元素,并去重
        union: function (a, b) {
            return this.unique(a.concat(b));
        },

        /**
         * 求两个集合的交集
         *
         * @param a 数组a
         * @param b 数组b
         * @returns {*[]} 返回处理后的数组
         */
        intersect: function (a, b) {
            //记录当前作用域的this
            let _this = this;
            a = this.unique(a);
            return this.map(a, function (ele) {
                return _this.contains(b, ele) ? ele : null;
            });
        },

        /**
         * 删除其中一个元素
         *
         * @param arr 数组
         * @param ele 指定元素值
         * @returns {*} 返回处理后的数组
         */
        remove: function (arr, ele) {
            let index = arr.indexOf(ele);//查元素下标
            if (index > -1) {
                arr.splice(index, 1);
            }
            return arr;
        },

        /**
         * 统计数组中某个值的出现次数
         *
         * @param arr 数组
         * @param value 数组中的某个元素
         */
        count: (arr, value) => arr.reduce((sum, currentEle) => currentEle === value ? sum + 1 : sum + 0, 0),

        /**
         * 将数组块划分为指定大小的较小数组。使用Array.from创建新数组，指定length为数组的长度。使用Array.slice将新数组的每个元素映射到size长度的块中。若原始数组不能均匀拆分, 则剩余的元素添加
         *
         * @param arr 数组元素
         * @param size 每个块的大小
         */
        block: (arr, size) => Array.from({length: Math.ceil(arr.length / size)}, (v, i) => arr.slice(i * size, i * size + size)),

        /**
         * 从数组中移除空值，空值包含 false、null、 0、 ""、undefined、NaN
         * filter(Boolean)等价于filter((item) => {return Boolean(item)})
         *
         * @param arr 数组
         * @returns {*} 返回处理后的数组 removeIfNull([0, 1, false, 2, '', 3, 'a', 'e' * 23, NaN, 's', 34]) -> [1, 2, 3, "a", "s", 34]
         */
        removeIfNull: (arr) => arr.filter(Boolean),

        /**
         * 求N个数的最大值
         *
         * @param arr 数组
         * @returns {number} 返回最大值元素
         */
        max: arr => Math.max.apply(null, arr),

        /**
         * 求N个数的最小值
         *
         * @param arr 数组
         * @returns {number} 返回最小值元素
         */
        min: arr => Math.min.apply(null, arr),

        /**
         * 求和
         *
         * @param arr 数组
         * @returns {number} 返回求和值
         */
        // sum:累加器,默认值为数组第一个元素值
        // currentEle:当前元素值
        sum: arr => arr.reduce((sum, currentEle) => sum + currentEle),

        /**
         * 求平均值
         *
         * @param arr 数组
         * @returns {number} 返回平均值
         */
        average: arr => this.sum(arr) / arr.length
    }

    /**
     * 数据类型校验工具对象
     */
    window.DataTypeUtils = {
        /**
         * 检测对象类型
         * @param obj 校验对象
         * @param type 对象类型
         * @returns {boolean} 若当前对象是指定的type类型,返回true,否则返回false
         */
        checkObjectType: function (obj, type) {
            return Object.prototype.toString.call(obj).slice(8, -1) === type;
        },
        /**
         * 是否为字符串类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isString: function (obj) {
            return this.checkObjectType(obj, 'String');
        },
        /**
         * 是否为数字类型（整数，小数）
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isNumber: function (obj) {
            return this.checkObjectType(obj, 'Number');
        },
        /**
         * 是否为Object对象类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isObject: function (obj) {
            return this.checkObjectType(obj, 'Object');
        },
        /**
         * 是否为数组类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isArray: function (obj) {
            return this.checkObjectType(obj, 'Array');
        },
        /**
         * 是否为日期对象类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isDate: function (obj) {
            return this.checkObjectType(obj, 'Date');
        },
        /**
         * 是否为布尔对象类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isBoolean: function (obj) {
            return this.checkObjectType(obj, 'Boolean');
        },
        /**
         * 是否为函数对象类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isFunction: function (obj) {
            return this.checkObjectType(obj, 'Function');
        },
        /**
         * 是否为Null类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isNull: function (obj) {
            return this.checkObjectType(obj, 'Null');
        },
        /**
         * 是否为Undefined类型
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isUndefined: function (obj) {
            return this.checkObjectType(obj, 'Undefined')
        },
        /**
         * 是否为False类型 (检验对象为空的情况)
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isEmpty: function (obj) {
            return (obj === '' || obj === undefined || obj === null || obj === 'null' || obj === 'undefined' || obj === 0 || obj === false || isNaN(obj));
        },
        /**
         * 是否为False类型 (检验对象不为空的情况)
         * @param obj 校验对象
         * @returns {boolean} true or false
         */
        isNotEmpty: function (obj) {
            return !this.isEmpty(obj);
        }
    }

    /**
     * 时间单位常量
     */
    const Unit = {
        "C0": 1,
        "C1": 1000,
        "C2": 1000 * 1000,
        "C3": 1000 * 1000 * 1000,
        "C4": 1000 * 1000 * 1000 * 60,
        "C5": 1000 * 1000 * 1000 * 60 * 60,
        "C6": 1000 * 1000 * 1000 * 60 * 60 * 24,
        WEEK: {
            'ZH': {
                "0": "星期日",
                "1": "星期一",
                "2": "星期二",
                "3": "星期三",
                "4": "星期四",
                "5": "星期五",
                "6": "星期六"
            },
            'EN': {
                "0": "Sunday",
                "1": "Monday",
                "2": "Tuesday",
                "3": "Wednesday",
                "4": "Thursday",
                "5": "Friday",
                "6": "Saturday"
            }
        },
        /**
         * 月、日、时、分、秒不足2位时，用0填充
         * @param value 月、日、时、分、秒值
         * @returns {string} 返回填充后的月、日、时、分、秒值
         */
        fillZero: function (value) {
            return value < 10 ? '0' + value : value;
        },
        /**
         * 换算时间值
         * @param d 日期值
         * @param m
         * @param max 最大值
         * @returns {number}
         */
        calcTime: function (d, m, max) {
            return d > max ? 0x7fffffffffffffff : d < -max ? 0x8000000000000000 : d * m;
        }
    }

    /**
     * 时间换算工具对象,可以对(天，小时，分钟，秒，毫秒，微妙，纳秒之间进行转换)
     */
    window.TimeUnit = {
        "DAYS": {
            "toNanos": function (d) { //将天数转换成纳秒
                return Unit.calcTime(d, Unit.C6 / Unit.C0, 0x7fffffffffffffff / (Unit.C6 / Unit.C0));
            },
            "toMicros": function (d) { //将天数转换成微秒
                return Unit.calcTime(d, Unit.C6 / Unit.C1, 0x7fffffffffffffff / (Unit.C6 / Unit.C1));
            },
            "toMillis": function (d) { //将天数转换成毫秒
                return Unit.calcTime(d, Unit.C6 / Unit.C2, 0x7fffffffffffffff / (Unit.C6 / Unit.C2));
            },
            "toSeconds": function (d) { //将天数转换成秒
                return Unit.calcTime(d, Unit.C6 / Unit.C3, 0x7fffffffffffffff / (Unit.C6 / Unit.C3));
            },
            "toMinutes": function (d) { //将天数转换成分钟
                return Unit.calcTime(d, Unit.C6 / Unit.C4, 0x7fffffffffffffff / (Unit.C6 / Unit.C4));
            },
            "toHours": function (d) { //将天数转换成小时
                return Unit.calcTime(d, Unit.C6 / Unit.C5, 0x7fffffffffffffff / (Unit.C6 / Unit.C5));
            },
            "toDays": function (d) {
                return d;
            }
        },
        "HOURS": {
            "toNanos": function (d) { //将小时转换成纳秒
                return Unit.calcTime(d, Unit.C5 / Unit.C0, 0x7fffffffffffffff / (Unit.C5 / Unit.C0));
            },
            "toMicros": function (d) { //将小时转换成微秒
                return Unit.calcTime(d, Unit.C5 / Unit.C1, 0x7fffffffffffffff / (Unit.C5 / Unit.C1));
            },
            "toMillis": function (d) { //将小时转换成毫秒
                return Unit.calcTime(d, Unit.C5 / Unit.C2, 0x7fffffffffffffff / (Unit.C5 / Unit.C2));
            },
            "toSeconds": function (d) { //将小时转换成秒
                return Unit.calcTime(d, Unit.C5 / Unit.C3, 0x7fffffffffffffff / (Unit.C5 / Unit.C3));
            },
            "toMinutes": function (d) { //将小时转换成分钟
                return Unit.calcTime(d, Unit.C5 / Unit.C4, 0x7fffffffffffffff / (Unit.C5 / Unit.C4));
            },
            "toHours": function (d) {
                return d;
            },
            "toDays": function (d) { //将小时转换成天
                return d / (Unit.C6 / Unit.C5);
            }
        },
        "MINUTES": {
            "toNanos": function (d) { //将分钟转换成纳秒
                return Unit.calcTime(d, Unit.C4 / Unit.C0, 0x7fffffffffffffff / (Unit.C4 / Unit.C0));
            },
            "toMicros": function (d) { //将分钟转换成微秒
                return Unit.calcTime(d, Unit.C4 / Unit.C1, 0x7fffffffffffffff / (Unit.C4 / Unit.C1));
            },
            "toMillis": function (d) { //将分钟转换成毫秒
                return Unit.calcTime(d, Unit.C4 / Unit.C2, 0x7fffffffffffffff / (Unit.C4 / Unit.C2));
            },
            "toSeconds": function (d) { //将分钟转换成秒
                return Unit.calcTime(d, Unit.C4 / Unit.C3, 0x7fffffffffffffff / (Unit.C4 / Unit.C3));
            },
            "toMinutes": function (d) {
                return d;
            },
            "toHours": function (d) { //将分钟转换成小时
                return d / (Unit.C5 / Unit.C4);
            },
            "toDays": function (d) { //将分钟转换成天
                return d / (Unit.C6 / Unit.C4);
            }
        },
        "SECONDS": {
            "toNanos": function (d) { //将秒转换成纳秒
                return Unit.calcTime(d, Unit.C3 / Unit.C0, 0x7fffffffffffffff / (Unit.C3 / Unit.C0));
            },
            "toMicros": function (d) { //将秒转换成微秒
                return Unit.calcTime(d, Unit.C3 / Unit.C1, 0x7fffffffffffffff / (Unit.C3 / Unit.C1));
            },
            "toMillis": function (d) { //将秒转换成毫秒
                return Unit.calcTime(d, Unit.C3 / Unit.C2, 0x7fffffffffffffff / (Unit.C3 / Unit.C2));
            },
            "toSeconds": function (d) {
                return d;
            },
            "toMinutes": function (d) { //将秒转换成分钟
                return d / (Unit.C4 / Unit.C3);
            },
            "toHours": function (d) { //将秒转换成小时
                return d / (Unit.C5 / Unit.C3);
            },
            "toDays": function (d) { //将秒转换成天
                return d / (Unit.C6 / Unit.C3);
            }
        },
        "MILLISECONDS": {
            "toNanos": function (d) { //将毫秒秒转换成纳秒
                return Unit.calcTime(d, Unit.C2 / Unit.C0, 0x7fffffffffffffff / (Unit.C2 / Unit.C0));
            },
            "toMicros": function (d) { //将毫秒秒转换成微秒
                return Unit.calcTime(d, Unit.C2 / Unit.C1, 0x7fffffffffffffff / (Unit.C2 / Unit.C1));
            },
            "toMillis": function (d) {
                return d;
            },
            "toSeconds": function (d) { //将毫秒秒转换成秒
                return d / (Unit.C3 / Unit.C2);
            },
            "toMinutes": function (d) { //将毫秒秒转换成分钟
                return d / (Unit.C4 / Unit.C2);
            },
            "toHours": function (d) { //将毫秒秒转换成小时
                return d / (Unit.C5 / Unit.C2);
            },
            "toDays": function (d) { //将毫秒秒转换成天
                return d / (Unit.C6 / Unit.C2);
            }
        },
        "MICROSECONDS": {
            "toNanos": function (d) { //将微秒秒转换成纳秒
                return Unit.calcTime(d, Unit.C1 / Unit.C0, 0x7fffffffffffffff / (Unit.C1 / Unit.C0));
            },
            "toMicros": function (d) {
                return d;
            },
            "toMillis": function (d) { //将微秒秒转换成毫秒
                return d / (Unit.C2 / Unit.C1);
            },
            "toSeconds": function (d) { //将微秒秒转换成秒
                return d / (Unit.C3 / Unit.C1);
            },
            "toMinutes": function (d) { //将微秒秒转换成分钟
                return d / (Unit.C4 / Unit.C1);
            },
            "toHours": function (d) { //将微秒秒转换成小时
                return d / (Unit.C5 / Unit.C1);
            },
            "toDays": function (d) { //将微秒秒转换成天
                return d / (Unit.C6 / Unit.C1);
            }
        },
        "NANOSECONDS": {
            "toNanos": function (d) {
                return d;
            },
            "toMicros": function (d) { //将纳秒秒转换成微秒
                return d / (Unit.C1 / Unit.C0);
            },
            "toMillis": function (d) { //将纳秒秒转换成毫秒
                return d / (Unit.C2 / Unit.C0);
            },
            "toSeconds": function (d) { //将纳秒秒转换成秒
                return d / (Unit.C3 / Unit.C0);
            },
            "toMinutes": function (d) { //将纳秒秒转换成分钟
                return d / (Unit.C4 / Unit.C0);
            },
            "toHours": function (d) { //将纳秒秒转换成小时
                return d / (Unit.C5 / Unit.C0);
            },
            "toDays": function (d) { //将纳秒秒转换成天
                return d / (Unit.C6 / Unit.C0);
            }
        }
    }

    /**
     * 日期工具对象
     */
    window.DateUtils = {
        /**
         * 格式化日期
         * format('2018-1-29', '{y}年{m}月{d} {h}时{M}分{s}秒 {w}') // -> 2021年08月28 09时07分49秒 星期六
         *
         * @param datetime 格式化时间
         * @param dateFormat 日期格式化, {y}-{m}-{d} {h}:{M}:{s} {w}
         * @return {null|string} 返回格式化后的日期字符串
         */
        format: function (datetime, dateFormat) {
            if (arguments.length === 0) {//不传参返回null
                return null;
            }
            //如果传递的参数是字符串或数字(时间戳)类型,则创建一个Date对象
            let date = DataTypeUtils.isString(datetime) || DataTypeUtils.isNumber(datetime) ? new Date(datetime) : datetime,
                formatObj = { //格式化标准对象
                    y: date.getFullYear(), //年
                    m: date.getMonth() + 1, //月
                    d: date.getDate(), //日
                    h: date.getHours(), //时
                    M: date.getMinutes(), //分
                    s: date.getSeconds(), //秒
                    w: Unit.WEEK.ZH[date.getDay()] //星期
                };
            //内容替换
            return (dateFormat || '{y}-{m}-{d} {h}:{M}:{s} {w}').replace(Regexps.RG_DATE_FORMAT_STANDARD, (result, key) => {
                return Unit.fillZero(formatObj[key]) || 0;
            });
        },
        /**
         * 格式化日期
         * @returns {string} 2021年08月28日 15时21分33秒 星期六
         */
        toLocaleDateTimeWeekString: function (date) {
            return this.format(date || new Date(), "{y}年{m}月{d} {h}时{M}分{s}秒 {w}");
        },
        /**
         * 格式化日期
         * @returns {string} 2021年08月28日 星期六
         */
        toDateString: function (date) {
            return this.format(date || new Date(), "{y}年{m}月{d} {w}");
        },
        /**
         * 格式化日期
         * @returns {string} 2021年08月28日 15:21:33 星期六
         */
        toLocaleDateTimeString: function (date) {
            return this.format(date || new Date(), "{y}年{m}月{d} {h}:{M}:{s} {w}");
        }
    }

    /**
     * Cookie工具对象
     */
    window.CookieUtils = {
        /**
         * 获取Cookie
         * @param cookieName cookie名
         * @returns {string|string} 返回指定的Cookie值
         */
        get: function (cookieName) {
            let val = document.cookie.match(new RegExp("(^| )" + cookieName + "=([^;]*)(;|$)"));//正则拼接
            return val != null ? val[2] : "";
        },
        /**
         * 设置Cookie
         * @param cookieName cookie名
         * @param cookieValue cookie值
         * @param cookieExpireTime 过期时间,-1表示永久不过期
         * @param unit 默认单位毫秒
         */
        set: function (cookieName, cookieValue, cookieExpireTime = -1, unit = TimeUnit.MILLISECONDS) {
            let date = new Date();//将date设置为指定的时间
            date.setTime((cookieExpireTime != null && cookieExpireTime !== -1) ? date.getTime() + unit.toMillis(cookieExpireTime) : -1);
            document.cookie = cookieName + "=" + cookieValue + ";expires=" + date.toGMTString();//设置cookie
        },
        /**
         * 删除Cookie
         * @param cookieName cookie名
         */
        remove: function (cookieName) {
            this.set(cookieName, null, Date.now() - Unit.C6);//设置成过期时间
        }
    }

    /**
     * local工具对象
     */
    window.LocalUtils = {
        /**
         * 设置一个或多个localStorage
         * @param key 键
         * @param value 值
         */
        set: function (key, value) {
            //处理多个key-value是一个对象的情况, {key1: value1, key2: value2, ...}
            let keys = arguments[0];//获取第一个参数
            if (DataTypeUtils.isObject(keys)) { //判断数据类型是否为数组
                for (let key in keys) {
                    localStorage.setItem(key, JSON.stringify(keys[key]))
                }
            } else { //处理key-value的case
                localStorage.setItem(key, JSON.stringify(value))
            }
        },

        /**
         * 获取一个或多个localStorage
         * @param key 键
         * @returns {null|any} 获取指定key的localStorage的值
         */
        get: function (key) {
            //处理多个key的情况, {key1,key2,key3,...}
            let keys = arguments[0];//获取第一个参数
            if (DataTypeUtils.isArray(key)) { //判断数据类型是否为数组
                let result = new Map();
                for (let ele of keys) {
                    result.set(ele, JSON.parse(localStorage.getItem(ele))); //不存在的key记录的值肯定为null
                }
                return result;//返回一个Map
            }
            return key ? JSON.parse(localStorage.getItem(key)) : null;
        },

        /**
         * 移除一个或多个localStorage
         * @param key 键
         */
        remove: function (key) {
            let keys = arguments[0];
            if (DataTypeUtils.isArray(keys)) { //判断数据类型是否为数组
                for (let ele of keys) {
                    localStorage.removeItem(ele);
                }
            } else {
                localStorage.removeItem(key);
            }
        },
        /**
         * 移除所有localStorage
         */
        clear: function () {
            localStorage.clear()
        }
    }

    /**
     * Session工具对象
     */
    window.SessionUtils = {
        /**
         * 设置一个或多个sessionStorage
         * @param key 键
         * @param value 值
         */
        set: function (key, value) {
            //处理多个key-value是一个对象的情况, {key1: value1, key2: value2, ...}
            let keys = arguments[0];//获取第一个参数
            if (DataTypeUtils.isObject(keys)) { //判断数据类型是否为数组
                for (let key in keys) {
                    sessionStorage.setItem(key, JSON.stringify(keys[key]))
                }
            } else { //处理key-value的case
                sessionStorage.setItem(key, JSON.stringify(value))
            }
        },

        /**
         * 获取一个或多个sessionStorage
         * @param key 键
         * @returns {null|Map} 获取指定key的localStorage的值
         */
        get: function (key) {
            //处理多个key的情况, {key1,key2,key3,...}
            let keys = arguments[0];//获取第一个参数
            if (DataTypeUtils.isArray(key)) { //判断数据类型是否为数组
                let result = new Map();
                for (let ele of keys) {
                    result.set(ele, JSON.parse(sessionStorage.getItem(ele))); //不存在的key记录的值肯定为null
                }
                return result;//返回一个Map
            }
            return key ? JSON.parse(sessionStorage.getItem(key)) : null;
        },

        /**
         * 移除一个或多个sessionStorage
         * @param key 键
         */
        remove: function (key) {
            let keys = arguments[0];
            if (DataTypeUtils.isArray(keys)) { //判断数据类型是否为数组
                for (let ele of keys) {
                    sessionStorage.removeItem(ele);
                }
            } else {
                sessionStorage.removeItem(key);
            }
        },
        /**
         * 移除所有sessionStorage
         */
        clear: function () {
            sessionStorage.clear()
        }
    }
    /**
     * 字符串工具对象
     */
    window.StringUtils = {
        isEmpty: function (str) {
            return str == null || str.length === 0;
        },
        isNotEmpty: function (str) {
            return !this.isEmpty(str);
        },
        isBlank: function (str) {
            return str == null || /^\s*$/.test(str);
        },
        isNotBlank: function (str) {
            return !this.isBlank(str);
        },
        trim: function (str) { //清除左右留白
            return str.replace(/^\s*|\s*$/g, '');
        },
        trimToEmpty: function (str) {
            return str == null ? "" : this.trim(str);
        },
        trimToAll: function (str) { //清除str左右留白和中间内容的留白
            return str.replace(/(^\s*)|(\s*$)|(\s*|\t|\r|\n)/g, '');
        },
        startsWith: function (str, prefix) {
            return str.indexOf(prefix) === 0;
        },
        endsWith: function (str, suffix) {
            return str.lastIndexOf(suffix) === (str.length - suffix.length);
        },
        contains: function (str, charSequence) {
            return str.indexOf(charSequence) >= 0;
        },
        equals: function (str1, str2) {
            return str1 && str2 && str1 === str2;
        },
        equalsIgnoreCase: function (str1, str2) {
            return str1 && str2 && typeof str1 == 'string' && typeof str2 == 'string' && str1.toLocaleLowerCase() === str2.toLocaleLowerCase();
        },
        containsWhitespace: function (str) { //是否包含空白字符
            return this.trimToAll(str);
        },
        /**
         * 生成size个字符
         * @param ch 字符
         * @param size 个数
         * @returns {string} 返回生成后的字符串
         */
        repeat: function (ch, size) {
            var result = "";
            for (let i = 0; i < size; i++) {
                result += ch;
            }
            return result;
        },
        /**
         * 向右填充size个padStr字符
         * @param str 原始字符串
         * @param size 填充字符个数
         * @param padStr 填充字符
         * @returns {string} 填充后的字符
         */
        rightPad: function (str, size, padStr) { //padStr填充字符
            return str + this.repeat(padStr, size);
        },
        /**
         * 向左填充size个padStr字符
         * @param str 原始字符串
         * @param size 填充字符个数
         * @param padStr 填充字符
         * @returns {string} 填充后的字符
         */
        leftPad: function (str, size, padStr) {
            return this.repeat(padStr, size) + str;
        },
        capitalize: function (str) { //首字母大写
            return this.isEmpty(str) ? str : str.replace(/^[a-z]/, function (matchStr) { //将匹配的字符串转成大写
                return matchStr.toUpperCase();
            });
        },
        uncapitalize: function (str) { //首字母小写
            return this.isEmpty(str) ? str : str.replace(/^[A-Z]/, function (matchStr) {
                return matchStr.toLowerCase();
            });
        },
        toggleCase: function (str) { //大写转小写，小写转大写
            return str.replace(/[a-z]/ig, function (matchStr) {
                if (/[A-Z]/.test(matchStr)) {
                    return matchStr.toLocaleLowerCase();
                }
                if (/[a-z]/.test(matchStr)) {
                    return matchStr.toLocaleUpperCase();
                }
            });
        },
        getSubStringCount: function (str, substring) { //统计含有的子字符串的个数
            if (this.isEmpty(str) || this.isEmpty(substring)) return 0;
            let count = 0, startIndex = 0;
            while ((startIndex = str.indexOf(substring, startIndex)) !== -1) {
                startIndex += substring.length;
                count++;
            }
            return count;
        },
        toUpper: function (str) {
            return str.toUpperCase();
        },
        toLower: function (str) {
            return str.toLowerCase();
        },
        isAlpha: function (str) { //只包含字母
            return /^[a-z]+$/i.test(str);
        },
        isAlphaSpace: function (str) { //只包含字母、空格
            return /^[a-z\s]*$/i.test(str);
        },
        isAlphanumeric: function (str) { //只包含字母、数字
            return /^[a-z0-9]+$/i.test(str);
        },
        isAlphanumericSpace: function (str) {//只包含字母、数字和空格
            return /^[a-z0-9\s]*$/i.test(str);
        },
        isNumeric: function (str) { //数字
            return /^(?:[1-9]\d*|0)(?:\.\d+)?$/.test(str);
        },
        isDecimal: function (str) { //小数
            return /^[-+]?(?:0|[1-9]\d*)\.\d+$/.test(str);
        },
        isNegativeDecimal: function (str) { //负小数
            return /^\-?(?:0|[1-9]\d*)\.\d+$/.test(str);
        },
        isPositiveDecimal: function (str) { //正小数
            return /^\+?(?:0|[1-9]\d*)\.\d+$/.test(str);
        },
        isInteger: function (str) { //整数
            return /^[-+]?(?:0|[1-9]\d*)$/.test(str);
        },
        isPositiveInteger: function (str) { //正整数
            return /^\+?(?:0|[1-9]\d*)$/.test(str);
        },
        isNegativeInteger: function (str) { //负整数
            return /^\-?(?:0|[1-9]\d*)$/.test(str);
        },
        isNumericSpace: function (str) { //只包含数字和空格
            return /^[\d\s]*$/.test(str);
        },
        isWhitespace: function (str) { //空白字符
            return /^\s*$/.test(str);
        },
        isAllLowerCase: function (str) { //是否全部小写
            return /^[a-z]+$/.test(str);
        },
        isAllUpperCase: function (str) { //是否全部大写
            return /^[A-Z]+$/.test(str);
        },
        defaultIfNull: function (str, defaultStr) { //若str字符串为null，则返回defaultStr串
            return str == null ? defaultStr : str;
        },
        defaultIfBlank: function (str, defaultStr) { //若str字符串满足isBlank条件，则返回defaultStr串
            return this.isBlank(str) ? defaultStr : str;
        },
        defaultIfEmpty: function (str, defaultStr) { //若str字符串满足isEmpty条件，则返回defaultStr串
            return this.isEmpty(str) ? defaultStr : str;
        },
        reverse: function (str) { //字符串反转,先将字符串转数组,反转之后在转成字符串
            return this.isBlank(str) ? str : str.split("").reverse().join("");
        },
        removeSpecialCharacter: function (str) { //删掉特殊字符(英文状态下)
            return str.replace(/[!-/:-@\[-`{-~]/g, "");
        },
        isSpecialCharacterAlphanumeric: function (str) { //只包含特殊字符、数字和字母（不包括空格，若想包括空格，改为[ -~]）
            return /^[!-~]+$/.test(str);
        },
        /**
         * 把连续出现的字母字符串进行压缩。如输入:aaabbbbcccccd 输出:3a4b5cd
         * @param str 字符串内容
         * @param ignoreCase 是否忽略大小写
         * @returns {string} 返回压缩后字符串
         */
        compressRepeatedStr: function (str, ignoreCase) {
            return this.isBlank(str) ? str : this.trimToAll(str).replace(new RegExp("([a-z])\\1+", ignoreCase ? "ig" : "g"), function (matchStr, group1) {
                return matchStr.length + group1;
            });
        },
        isChinese: function (str) { //中文校验
            return /^[\u4E00-\u9FA5]+$/.test(str);
        },
        removeChinese: function (str) { //去掉中文字符
            return str.replace(/[\u4E00-\u9FA5]+/gm, "");
        },
        /**
         1、正则表达式：/(.)(?=.*\1)/g;
         2、.匹配任意字符，但只能匹配任意字符中的一个;
         3、(.)加上()就是将匹配的该字符存储起来供以后引用;
         4、(?=)预搜索（也有叫断言的，也有叫预查的），指明某个字符的右侧是什么，但不包含这部分，只取这个‘某个字符’,如：p(?=ing)匹配字符串ping时匹配成功，但匹配到的字符是p不是ping;
         5、(?=.*\1) 这个\1就是指的前面(.)的这个字符，之前说它被加上小括号就是被存储起来了，现在\1就是取存储的第一个（共一个），*匹配次数，指出现任意次，.*指出现任意次任意字符
         6、(.)(?=.*\1)指第一个匹配字符，如果右侧出现的内容中包含该字符时就匹配上该字符;
         7、g全局匹配模式，匹配所有字符串;
         8、这个去重的结果其实是倒着来排序的，就是说重复字符出现在前面的都被置空了，是按一个字符从后往前出现的顺序排的。
         * @param str 字符串
         * @returns {string} 返回去重后的字符串
         */
        removeDuplicate: function (str) {
            return str.replace(/(.)(?=.*\1)/g, '');
        }
    }

    /**
     * 计时器操作工具对象 Stopwatch
     */
    window.Stopwatch = {
        "value": 0,
        /**
         * 开始计时
         * @returns {Stopwatch} 返回当前计时对象
         */
        "createStarted": function () {
            this.value = new Date().getTime();
            return this;
        },
        /**
         * 终止计时
         * @returns {number} 返回总计运行时间（单位:毫秒）
         */
        "elapsed": function () {
            return new Date().getTime() - this.value;
        },
        /**
         * 倒计时
         * @param startTime 开始时间
         * @param el 绑定的DOM对象
         * @param text 倒计时结束显示文本
         */
        "countDown": function (startTime, el, text) {
            console.log(TimeUnit.DAYS.toMillis(1))
            let t = startTime;
            let dom = document.querySelector(el);
            let timer = setInterval(() => {
                dom.innerText = "倒计时：" + t + "秒";
                if (--t < 0) {
                    t = startTime;
                    clearInterval(timer);//清除定时器（表示执行了time秒后结束执行）
                    dom.innerText = text || "倒计时结束!";
                }
            }, Unit.C1);
        }
    }
//======================================================获取DOM节点方法======================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================
    /**
     * 根据类名获取
     * @param className 类名
     * @returns {[]|HTMLCollectionOf<Element>} DOM节点对象
     */
    window.byClass = function (className) {
        if (document.getElementsByClassName) {
            return document.getElementsByClassName(className.substring(1));
        } else {
            let nodeList = [], //存DOM节点集合
                tagList = byTagName("*"), //获取页面中的所有的元素
                reg = new RegExp('(^|\\s)' + className.substring(1) + '($|\\s)', 'g'); //正则匹配对应的标签
            for (let tag of tagList) {
                //若标签存在class名称就push到一个数组中
                if (reg.test(tag.className)) {
                    nodeList.push(tag);
                }
            }
            return nodeList;
        }
    }
    /**
     * 根据类名获取
     * @param idName 类名
     * @returns {HTMLElement} DOM节点对象
     */
    window.byId = function (idName) {
        return document.getElementById && document.getElementById(idName);
    }
    /**
     * 根据类名获取
     * @param name 类名
     * @returns {[]|HTMLCollectionOf<Element>} DOM节点对象
     */
    window.byName = function (name) {
        return document.getElementsByName && document.getElementsByName(name);
    }
    /**
     * 根据类名获取
     * @param tagName 类名
     * @returns {[]|HTMLCollectionOf<Element>} DOM节点对象
     */
    window.byTagName = function (tagName) {
        return document.getElementsByTagName && document.getElementsByTagName(tagName);
    }
//======================================================简单封装Ajax======================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================
    /**
     * 分析：首先是要有默认的参数集合，比如说默认get请求，默认是异步请求方式等
     * 原生Ajax实现步骤:
     * (1)创建XMLHttpRequest对象，IE6以下使用ActiveXObject对象创建
     * (2)响应的数据类型，responseText 或 responseXML
     * (3)定义监听readyState属性状态的事件函数 onreadystatechange
     * (4)open,send方法收尾,注意get/post请求时的不同情况
     *
     * status状态：
     * 1XX：信息性状态码，表示接收的请求正在处理
     * 2XX：成功状态码，表示请求正常处理
     * 3XX：重定向状态码，表示需要附加操作来完成请求
     * 4XX：客户端错误状态，表示服务器无法处理请求
     * 5XX：服务器错误状态，表示服务器处理请求出错
     */
    window.$.ajax = function (obj = {}) {
        let flag = true;//只执行一次complete函数
        //默认参数集合
        let params = {
            type: (arguments[0].type || "POST").toUpperCase(),//默认是get请求
            url: arguments[0].url || "",
            async: arguments[0].async || true,//默认是异步请求
            dataType: arguments[0].dataType || "text",//默认响应数据是文本
            data: arguments[0].data || {}, //传递参数
            contentType: arguments[0].contentType || "application/x-www-form-urlencoded;charset=utf-8",//默认提交方式
            beforeSend: arguments[0].beforeSend || function () {
                console.log("beforeSend");
            },//向服务器发送请求前要执行代码
            complete: arguments[0].complete || function () {
                console.log("complete");
            },//执行完成后的回调函数
            success: arguments[0].success || function () {
                console.log("success");
            },//执行成功回调函数
            error: arguments[0].error || function () {
            }//执行失败回调函数
        };
        if (StringUtils.equalsIgnoreCase(params.dataType, "json")) {
            params.contentType = "application/json;charset=utf-8";
        }
        //发送请求前需要执行的代码
        params.beforeSend();
        //创建请求
        let xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
        //设置响应类型, https://blog.csdn.net/yudldl/article/details/83095523
        // xmlhttp.responseType = params.dataType;
        //判断POST方式或GET方式提交
        if (StringUtils.equals(params.type, 'POST')) {
            xmlhttp.open(params.type, params.url, params.async);
            xmlhttp.setRequestHeader("Content-Type", params.contentType);
            xmlhttp.send(_serialize_(params.data));//序列化参数成 username=aitao&password=1234 的字符串数据
        } else if (StringUtils.equals(params.type, 'GET')) {
            xmlhttp.open(params.type, params.url + "?" + _serialize_(params.data), params.async);
            xmlhttp.send();
        }
        // 异步请求时需要定义onreadstatechange事件函数，同步请求时则不需要定义该事件函数
        xmlhttp.onreadystatechange = params.async ? function () {
            if (xmlhttp.readyState === 4) {
                if (xmlhttp.status >= 200 && xmlhttp.status < 300 || xmlhttp.status === 304) {
                    if (flag) {
                        params.complete();
                        flag = false;
                    }
                    let responseData = xmlhttp.response;
                    if (StringUtils.equals(params.dataType, "json")) {
                        responseData = JSON.parse(responseData);
                    }
                    //response返回任意类型,任意类型取决于responseType值, responseText返回文本text类型, responseXML返回xml对象类型
                    params.success && params.success(responseData);//执行成功回调函数,并返回响应数据
                } else {
                    params.error && params.error(xmlhttp.response); //执行失败回调函数
                }
            }
            // else if (xmlhttp.readyState === 1 || xmlhttp.readyState === 2 || xmlhttp.readyState === 3) {
            //     //当readyState的状态是 1 2 3时，会执行该函数
            //     params.error && params.error(xmlhttp.response); //执行失败回调函数
            // }
        } : null;
    };

//======================================================私有化方法======================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================
    /**
     * 序列化传递数据（将{username:aitao,password:123} 转换成 "username=aitao&password=123"的格式数据）
     * 分析：只有当传递的data参数是一个对象时，才将其进行转换，其它类型数据原样返回
     * @param data 转换的数据
     * @returns {*} 返回转换后的数据
     * @private
     */
    function _serialize_(data) {
        if (typeof data === "object") {
            let str = "";
            for (let key in data) {
                str += key + "=" + data[key] + "&";
            }//username=aitao&password=123&
            return str.substring(0, str.length - 1);
        } else {
            return data;
        }
    }

    /**
     * 检测dom对象是否存在指定的类名
     * @param dom DOM对象
     * @param className 类名
     * @returns {boolean} true or false
     * @private
     */
    function _hasClass_(dom, className) {
        return new RegExp('(\\s|^)' + className + '(\\s|$)').test(dom.className);
    }

    /**
     * 给指定的dom对象添加类名
     * @param dom DOM对象
     * @param className 类名
     * @returns {boolean} true or false
     * @private
     */
    function _insertClass_(dom, ...className) {
        let isSuccess = false;
        for (let name of className) {
            if (!this.hasClass(dom, name)) {
                dom.className += " " + name;
                isSuccess = true;
            }
        }
        return isSuccess;
    }

    /**
     * 删除dom对象的指定类名
     * @param dom DOM对象
     * @param className 类名
     * @private
     */
    function _deleteClass_(dom, ...className) {
        let isSuccess = false;
        for (let name of className) {
            if (this.hasClass(dom, name)) {
                dom.className = dom.className.replace(new RegExp('(\\s|^)' + name + '(\\s|$)'), ' ');
                isSuccess = true;
            }
        }
        dom.className = StringUtils.trim(dom.className);
        return isSuccess;
    }

    /**
     * 替换DOM对象的指定类名
     * @param dom DOM对象
     * @param oldClassName 旧类名
     * @param newClassName 新类名
     * @private
     */
    function _replaceClass_(dom, oldClassName, newClassName) {
        this.removeClass(dom, oldClassName);
        this.addClass(dom, newClassName);
        dom.className = StringUtils.trim(dom.className);
    }

    /**
     * 获取或设置文本内容（包含标签）
     * @returns {_html_|*} 传递参数值,则获取指定DOM对象的值,否则设置指定DOM对象的值
     * @private
     */
    function _html_() {
        if (arguments[0]) {
            this.el.innerHTML = arguments[0];
            return this;
        }
        return this.el.innerHTML;
    }

    /**
     * 获取或设置文本内容（忽略标签）
     * @returns {string|*|string|_text_} 传递参数值,则获取指定DOM对象的值,否则设置指定DOM对象的值
     * @private
     */
    function _text_() {
        if (arguments[0]) {
            this.el.innerText = arguments[0];
            return this;
        }
        return this.el.innerText;
    }

    /**
     * 获取表单之类的DOM对象值
     * @returns {*|_val_} 传递参数值,则获取指定DOM对象的值,否则设置指定DOM对象的值
     * @private
     */
    function _val_() {
        if (arguments[0]) {
            this.el.value = arguments[0];
            return this;
        }
        return this.el.value;
    }

    /**
     * 给指定DOM对象设置属性和值
     * 分析：
     * (1) 当只有一个参数时，若该参数是字符串值，那么就是一个键名，通过键名获取该键对应的值；若该参数是一个对
     *      象，对象的键值对即为为当前DOM对象设置的多个属性和属性值。
     * (2) 当有两个参数时，第一个参数代表属性，第二个参数代表属性值
     * @param attr 属性名
     * @param value 属性值
     * @returns {string|_attr_} 若只传递attr属性，则获取DOM对象指定的属性值,否则给DOM对象设置指定的属性和值
     * @private
     */
    function _attr_(attr, value) {
        let len = arguments.length;
        if (len === 1) {
            if (typeof attr === "string") {
                return this.el.getAttribute(attr);
            } else if (typeof attr === "object") {
                for (let key in attr) {
                    this.el.setAttribute(key, attr[key]);
                }
                return this;
            }
        }
        if (len === 2) {
            this.el.setAttribute(attr, value);
            return this;
        }
    }

    /**
     * 删除指定的属性
     * @param attr 属性名
     */
    function _removeAttr_(attr) {
        this.el.removeAttribute(attr);
    }

    /**
     * 添加一个或多个class类名
     * @param className 类名
     */
    function _addClass_(...className) {
        for (let i = 0; i < className.length; i++) {
            this.el.classList.add(className);
        }
    }

    /**
     * 删除一个或多个class类名
     * @param className 类名
     */
    function _removeClass_(...className) {
        for (let i = 0; i < className.length; i++) {
            this.el.classList.remove(className[i]);
        }
    }

    /**
     * 切换类名,若DOM对象已经存在className则删除,不存在则添加
     * @param className 类名
     */
    function _toggleClass_(className) {
        this.el.classList.toggle(className);
    }


    /**
     * 获取当前dom对象的所有兄弟节点
     *      1    元素节点
     *      2    属性节点
     *      3    文本节点
     *      8    注释节点
     *      9    document节点
     * @returns {[]} 返回当前DOM节点的所有兄弟节点
     */
    function _siblings_() {
        let result = [];
        if (this.el == null) {
            alert("DOM对象值为空")
            return result;
        }
        let children = this.el.parentNode.children;
        for (let el of children) {
            if (el !== this.el && el.nodeType === 1) {
                result.push(el);
            }
        }
        return result;
    }

    /**
     * 添加事件兼容,绑定多个事件
     * @param obj 绑定的对象
     * @param eventType 绑定事件对象, click,mouseenter,change等
     * @param callback 回调函数
     * @private
     */
    function _bindEvent_(obj, eventType, callback) {
        return obj.addEventListener ? obj.addEventListener(eventType, callback) : obj.attachEvent("on" + eventType, callback);
    }

    /**
     * 解析User-Agent信息
     * @param agent user-agent信息
     * @param browserTypeNameList 浏览器类型名
     * @param regexList 正则表达式集合
     * @private
     */
    function _resolveUserAgentInfo_(agent, browserTypeNameList, regexList) {
        let result = [];
        let systemInfo = agent.split(' ')[1].split(' ')[0].split('(')[1];
        result.push(systemInfo);
        console.log(systemInfo)
        for (let i = 0; i < browserTypeNameList.length; i++) {
            if (StringUtils.equals(browserTypeNameList[i], "safari")) {
                if (!(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0)) {
                    continue;
                }
            }
            if (agent.indexOf(browserTypeNameList[i]) > 0) {
                result.push(agent.match(regexList[i])[0].split('/')[0]);
                result.push(agent.match(regexList[i])[0].split('/')[1]);
                return result;
            }
        }
        result.push('请更换chrome,firefox,opera,safari,IE,Edge等主流浏览器进行操作')
        return [result];
    }
})(window);