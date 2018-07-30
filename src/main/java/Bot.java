import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(Bot.class);

    private RequestHandler requestHandler = new RequestHandler();

    public void onUpdateReceived(Update update) {

        // переменные из апдейта для использования
        String text = update.getMessage().getText();
        String userName = update.getMessage().getChat().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        // если /start - приветствие + кнопки
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (text.equals("/start")) {
                sendMsg(update.getMessage().getChatId().toString(),
                        "Привет, я могу сказать что-то о последних катках в дотан кое-каких поцанов D:");
                sendButtons(update.getMessage().getChatId().toString());

            }
        }

        //обработка кнопок
        switch (text) {
            case "Making Cookies Having Teas.":
            case "ATHLETE!":
            case "фкыруевмило":
            case "Milonov":
            case "ANYA II. BACK TO DOTA":
                try {
                    Response response = requestHandler.process(update.getMessage().getText());
                    StringBuilder sb = new StringBuilder(text)
                            .append(response.getHeader())
                            .append(response.getTimeOfLastGame())
                            .append("\n")
                            .append(response.getMiddler())
                            .append("\n")
                            .append("1. ")
                            .append(response.getFirstGameHero())
                            .append(", ")
                            .append(response.getFirstGameSide())
                            .append(", ")
                            .append("*")
                            .append(response.getFirstGameResult())
                            .append("*")
                            .append(", ")
                            .append(response.getFirstGameLobby())
                            .append(", ")
                            .append(response.getFirstGameDuration())
                            .append(", ")
                            .append("\n")
                            .append("2. ")
                            .append(response.getSecondGameHero())
                            .append(", ")
                            .append(response.getSecondGameSide())
                            .append(", ")
                            .append("*")
                            .append(response.getSecondGameResult())
                            .append("*")
                            .append(", ")
                            .append(response.getSecondGameLobby())
                            .append(", ")
                            .append(response.getSecondGameDuration());


                    String message = sb.toString();

                    sendMsg(update.getMessage().getChatId().toString(), message);

                    logger.info("Запрос по " + text + " от " + userName + " | " + firstName+ " | " + lastName);
                } catch (IOException e) {
                    logger.error(e.getStackTrace());
                }
                break;
            case "Bird is the word": {
                String message = "Хер тут, этот нехороший человек так и не поставил галочку какую-то там, чтобы шарить свою статистику.";
                sendMsg(update.getMessage().getChatId().toString(), message);
                logger.info("Запрос по " + text + " от " + userName + " | " + firstName);
                break;
            }
//            case "LOOOOOOOOOOOOOOOOOOM": {
//                String message = "И тут не работает пока, играет во всякие говно-моды, они ломают ботика(";
//                sendMsg(update.getMessage().getChatId().toString(), message);
//                logger.info("Запрос по " + text + " от " + userName + " | " + firstName);
//                break;
//            }
        }
    }


    private void sendButtons(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери поцана / девченку:");

        // Объект кнопок
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Сами кнопки \ лист рядов кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Создать один ряд кнопок
        KeyboardRow row = new KeyboardRow();
        // Кнопки
        row.add("Making Cookies Having Teas.");
        row.add("ATHLETE!");
        row.add("фкыруевмило");
        // Добавляем в лист
        keyboard.add(row);
        // Второя ряд
        row = new KeyboardRow();
        // Кнопки
        row.add("Milonov");
        row.add("ANYA II. BACK TO DOTA");
        row.add("Bird is the word");
        // Добавить в лист
        keyboard.add(row);
        // Добавить лист в объект
        keyboardMarkup.setKeyboard(keyboard);
        // Положить всё в сообщение
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.getStackTrace());
        }
    }


    private synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getStackTrace());
        }
    }

    public String getBotUsername() {
        return "Botik";
    }

    public String getBotToken() {
        return "674228669:AAGo5Rw0URRDDrjyzGztO--GM14JG6Y7s9M";
    }
}