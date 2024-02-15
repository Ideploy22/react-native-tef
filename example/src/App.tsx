import React, { useEffect, useState } from 'react';

import {
  StyleSheet,
  View,
  Text,
  TextInput,
  Button,
  FlatList,
  DeviceEventEmitter,
  KeyboardAvoidingView,
} from 'react-native';
import {
  configTef,
  onInitTef,
  payCred,
  payDeb,
  payPix,
} from 'react-native-ideploy-tef';

export default function App() {
  const [name, setName] = useState('IdeployTef');
  const [version, setVersion] = useState('1.0.0');
  const [pinpad, setPinpad] = useState('pinpad');
  const [pinPadText, setPinPadText] = useState('');
  const [typePayment, setTypePayment] = useState('debit' || 'credit' || 'pix');
  const [value, setValue] = useState(100);
  const [doc, setDoc] = useState('49.984.096/0001-69');

  const [results, setResults] = useState<
    {
      messageEgin?: string;
      message: string;
      type: string;
    }[]
  >([]);

  function onStartConfig() {
    configTef({
      doc,
      name,
      pinPadText,
      pinpad,
      version,
    });
  }

  useEffect(() => {
    onInitTef();

    const eventTef = DeviceEventEmitter.addListener('ideploy.tef', (event) => {
      console.log('#################################################');
      console.log('__________ Event _________________');
      console.log(event);
      console.log('#################################################');

      setResults((prev) => [event, ...prev]);
    });

    return () => {
      eventTef.remove();
    };
  }, []);

  return (
    <KeyboardAvoidingView style={styles.container} behavior="padding">
      <View style={styles.container}>
        <View style={styles.box}>
          <TextInput
            style={styles.input}
            value={name}
            onChangeText={setName}
            placeholder="Name"
          />
          <TextInput
            style={styles.input}
            value={version}
            onChangeText={setVersion}
            placeholder="Version"
          />
          <TextInput
            style={styles.input}
            value={pinpad}
            onChangeText={setPinpad}
            placeholder="Pinpad"
          />
          <TextInput
            value={pinPadText}
            style={styles.input}
            onChangeText={setPinPadText}
            placeholder="PinPadText"
          />
          <TextInput
            style={styles.input}
            value={doc}
            onChangeText={setDoc}
            placeholder="Doc"
          />
          <Button title="Config" onPress={onStartConfig} />
          <TextInput
            style={styles.input}
            value={value.toString()}
            onChangeText={(text) => setValue(Number(text))}
            placeholder="Value"
          />
          <View style={{ flexDirection: 'row' }}>
            <Button
              color={typePayment === 'debit' ? 'green' : 'gray'}
              title="Debit"
              onPress={() => {
                setTypePayment('debit');
              }}
            />
            <Button
              color={typePayment === 'credit' ? 'green' : 'gray'}
              title="Credit"
              onPress={() => {
                setTypePayment('credit');
              }}
            />
            <Button
              color={typePayment === 'pix' ? 'green' : 'gray'}
              title="Pix"
              onPress={() => {
                setTypePayment('pix');
              }}
            />
          </View>
          <Button
            title="Pay"
            onPress={() => {
              if (typePayment === 'debit') {
                payDeb(value);
              } else if (typePayment === 'credit') {
                payCred(value, '1', '1');
              } else if (typePayment === 'pix') {
                payPix(value);
              }
            }}
          />
        </View>
        <View style={styles.box}>
          <Text>Result: test</Text>
          <FlatList
            data={results}
            renderItem={({ item, index }) => (
              // <Text>
              //   {item.type} - {item.message} - {item.messageEgin}
              // </Text>
              <View
                style={{
                  backgroundColor: index % 2 === 0 ? 'gray' : 'black',
                  padding: 10,
                  marginVertical: 5,
                }}
              >
                <Text style={{ color: 'white' }}>{item.type}</Text>
                <Text style={{ color: 'white' }}>{item.message}</Text>
                <Text style={{ color: 'white' }}>{item.messageEgin}</Text>
              </View>
            )}
            keyExtractor={(_, index) => index.toString()}
          />
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
  },
  box: {
    padding: 20,
    width: '50%',
    marginVertical: 20,
  },
  input: {
    width: '100%',
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginVertical: 10,
  },
});
