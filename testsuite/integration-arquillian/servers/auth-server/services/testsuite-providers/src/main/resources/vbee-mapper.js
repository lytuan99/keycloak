var ipAddress = userSession.getIpAddress();
var phoneNumber = user.getFirstAttribute('phoneNumber');
var avatar = user.getFirstAttribute('avatar');
var identityProvider = userSession.getNote('identity_provider');

// map value to token claims
token.getOtherClaims().put("ip", ipAddress);
token.getOtherClaims().put("phone_number", phoneNumber);
token.getOtherClaims().put("avatar", avatar);
token.getOtherClaims().put("identity_provider", identityProvider || 'email');
