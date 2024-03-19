import SmCrypto from 'sm-crypto'

const sm2 = SmCrypto.sm2
const sm4 = SmCrypto.sm4

/** 公钥 */
const publicKey =
  '04bc9b314bc317dfe0d7672126815b2e6d9cf150efbb9a933fe1d8b4ef70e9873e6c68dfe57e1d2f1870dcba4413119ad0851fee24da487762189211bc7e7632d0'
/** sm4key */
const key = '68677a707e4031323334353637383930'

/**
 * sm2公钥加密
 * @param txt
 * @returns
 */
export const sm2Encrypt = (txt: string) => {
  return '04' + sm2.doEncrypt(txt, publicKey)
}

/**
 * sm4加密
 * @param txt
 * @returns
 */
export const sm4Encrypt = (txt: string) => {
  return sm4.encrypt(txt, key)
}

/**
 * sm4解密
 * @param txt
 * @returns
 */
export const sm4Decrypt = (txt: string) => {
  return sm4.decrypt(txt, key)
}
