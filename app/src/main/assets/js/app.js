function setImage(id) {
    // console.log("setImage id=" + id);
    var img = $("#" + id);
    if (img) {
        var src = img.attr("src");          // 此时应该 src == srcNone
        var srcNone = img.data("none");     // data-none 默认图片地址
        var srcCache = img.data("cache");   // data-cache 本地图片地址
        var srcData = img.data("src");      // data-src 原始网络地址

        // console.log("setImage src=" + src);
        // console.log("setImage srcNone=" + srcNone);
        // console.log("setImage srcCache=" + srcCache);
        // console.log("setImage srcData=" + srcData);
        if (src == srcNone) {
            img.attr("src", srcCache);
        }
    }
}

// init set content
function initBody() {
    // console.log("initBody()");
    var pageContent = $("#content");
    if (window.app) {
        pageContent.html(window.app.getContent());
        $('pre code').each(function (i) {
            hljs.highlightBlock(this);
        });
        $('.at_user').click(function (i) {
            var name = $(this).attr("title");
            if (window.app) {
                window.app.onAtUserClicked(name);
            }
        });
    }
}

// callback ,on img clicked
function onImageClicked(img) {
    // console.log("onImageClicked() img=" + img);
    var id = img.id;
    if (window.app) {
        window.app.onImageClicked(id);
    }
}

// callback, on page ready
function onWebReady() {
    // console.log("onWebReady()");
    if (window.app) {
        window.app.onWebReady();
    }
}

// init complete, page is ready
function onReady() {
    // console.log("onReady()");
    initBody();
    onWebReady();
}

//window.onload=initialize
Zepto(function ($) {
    onReady();
})
