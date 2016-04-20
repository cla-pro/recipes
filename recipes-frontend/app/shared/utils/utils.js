function isObjectEmpty(obj) {
    return obj === undefined || obj === null || obj === '';
};

function isObjectNotEmpty(obj) {
    return !isObjectEmpty(obj);
};