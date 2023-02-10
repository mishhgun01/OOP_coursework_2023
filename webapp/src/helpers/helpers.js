export default function hash(password) {
    var hash = 0, i, chr;
    if (password.length === 0) return hash;
    for (i = 0; i < password.length; i++) {
        chr   = password.charCodeAt(i);
        hash  = ((hash << 5) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
    }
    return hash.toString();
};