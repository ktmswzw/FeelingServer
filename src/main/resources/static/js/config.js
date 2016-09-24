/**
 * Created by  moxz on 2015/2/4.
 */
require.config({
    baseUrl: '/js/',
    config: {
        moment: {
            noGlobal: true
        }
    },
    paths: {
        jquery: 'libs/jquery-2.1.4',
        comm: 'comm',
        async: 'libs/async',
    },
    shim: {
        comm: { deps: ['jquery']  , exports :'comm'}
    }
});