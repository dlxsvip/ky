// 加载 js|css 服务
App.service('loadFileService', [function () {
    var loadFileService = {
        loadedFiles : []
    };

    // 加载 js|css
    loadFileService.loadfile = function (filePath, fileType) {
        var file_ref;
        if (fileType == "js") {
            file_ref = document.createElement('script');
            file_ref.setAttribute("type", "text/javascript");
            file_ref.setAttribute("src", filePath);
        } else if (fileType == "css") {
            file_ref = document.createElement('link');
            file_ref.setAttribute("type", "text/css");
            file_ref.setAttribute("rel", "stylesheet");
            file_ref.setAttribute("href", filePath);
        }
        if (file_ref) {
            for (var i = 0; i < this.loadedFiles.length; i++) {
                if (this.loadedFiles[i] === filePath) {
                    return;
                }
            }
            this.loadedFiles.push(filePath);
            document.getElementsByTagName("head")[0].appendChild(file_ref);
        }
    };


    return loadFileService;
}]);