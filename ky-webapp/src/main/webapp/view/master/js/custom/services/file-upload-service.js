App.factory('fileUploadService', ['$http', '$q', 'urlService', function ($http, $q, urlService) {
    var fileUploadService = {};

    fileUploadService.uploadImage = function(formData) {
        var api = urlService.getFullUrl("file/upload");
        var deffered = $q.defer();

        $http({
            method: 'POST',
            url: api,
            headers: {'Content-Type': undefined},
            data: formData,
            transformRequest: function(data, headersGetterFunction) {
                return data;
            }
        })
        .then(function(response) {
            deffered.resolve(response.data);
        }, function() {
            deffered.reject();
        });


        return deffered.promise;

    };

    fileUploadService.checkUploadImage = function(file) {
        var result = {code: 0, message: 'ok'};
        var maxSize = 2*1024*1024;
        var fileType = file.type.toLowerCase();

        if (file.size > maxSize) {
            result.code = 1;
            result.message = "请上传小于2MB的图片";
            return result;
        }

        if ((fileType !== 'image/png') &&
            (fileType !== 'image/jpg') &&
            (fileType !== 'image/bmp') &&
            (fileType !== 'image/jpeg')) {
            result.code = 1;
            result.message = "不支持的图片格式，目前支持的图片格式包括jpg/png/bmp/jpeg";
            return result;
        }

        return result;

    };

    return fileUploadService;
}]);
