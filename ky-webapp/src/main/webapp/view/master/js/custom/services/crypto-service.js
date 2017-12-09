/**
 * Created by yl on 2017/8/11.
 */
App.service('cryptoService', [function () {
        var cryptoService = {};

        cryptoService.aesEncrypt = function (text) {
            return Ics.createNew().aesEncrypt(text);
        };

        return cryptoService;
    }
]);