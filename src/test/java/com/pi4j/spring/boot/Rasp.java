/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.pi4j.spring.boot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

//Raspberry Server ,accept action 1/0
public class Rasp {

	public static final GpioController gpio = GpioFactory.getInstance();

	public static void main(String[] args) {
		ServerSocket server = null;
		Socket you = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "", PinState.LOW);
		try {
			server = new ServerSocket(8970);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("Wait.....");
			you = server.accept();
			in = new DataInputStream(you.getInputStream());
			out = new DataOutputStream(you.getOutputStream());
			while (true) {
				int i = in.readInt();
				System.out.println(i);
				// GPIO Control
				if (i == 1) {
					led.high();
					out.writeUTF("LED ON");
				} else if (i == 0) {
					led.low();
					out.writeUTF("LED OFF");
				} else
					out.writeUTF("Invalid");
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}