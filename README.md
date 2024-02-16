# react-native-ideploy-tef

tef elgin

## Installation

```sh
npm install react-native-ideploy-tef
```

## Android

add settings.gradle

 ```gradle
include (':elgin-core')
project(':elgin-core').projectDir = file('../node_modules/react-native-ideploy-tef/libs/hom/elgin-core')
  ```

## Usage


```js
import {
  configTef,
  onCancel,
  onInitTef,
  payCred,
  payDeb,
  payPix,
} from 'react-native-ideploy-tef';

// ...

const result = multiply(3, 7);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
