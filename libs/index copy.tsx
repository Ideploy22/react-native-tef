// import type { IConfigTef } from './IIdeployTef';

const IdeployTef = require('./NativeIdeployTef').default;

export function onInitTef(): void {
  IdeployTef.onInitTef();
}

export function configTef({
  name,
  vesion,
  pinpad,
  pinPadText,
  doc,
}: any): void {
  IdeployTef.configTef(name, vesion, pinpad, pinPadText, doc);
}
