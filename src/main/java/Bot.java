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
        String chatIdForReply = update.getMessage().getChatId().toString();

        // если /start - приветствие + команды
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (text) {
                case "/start":
                    sendMsg(chatIdForReply,
                            "Привет, я могу сказать что-то о последних катках в дотан кое-кого (команда */last | /l*)," +
                                    "или проверить их на он-лайн (команда */check | /c*)");
                    sendButtonsWithCommands(chatIdForReply);
                    break;
                case "/last":
                case "/l":
                    sendButtonsWithNicksForLast(chatIdForReply);
                    break;
                case "/check":
                case "/c":
                    sendButtonsWithNicksForCheck(chatIdForReply);
                    break;
            }
        }

        //обработка кнопок
        switch (text) {
            case "/l Making Cookies Having Teas.":
            case "/l ATHLETE!":
            case "/l фкыруевмило":
            case "/l Milonov":
            case "/l ANYA II. BACK TO DOTA":
                try {
                    Response response = requestHandler.process(text);
                    StringBuilder sb = new StringBuilder(text.substring(3))
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

                    sendMsg(chatIdForReply, message);

                    logger.info("Запрос по " + text + " от " + userName + " | " + firstName + " | " + lastName);
                } catch (IOException e) {
                    logger.error(e.getStackTrace());
                }
                break;
            case "/l Bird is the word": {
                String message = "Не покажу, Этот нехороший человек так и не поставил галочку какую-то там, чтобы шарить свою статистику.";
                sendMsg(chatIdForReply, message);
                logger.info("Запрос по " + text + " от " + userName + " | " + firstName);
                break;
            }
            case "/c Making Cookies Having Teas.":
            case "/c ATHLETE!":
            case "/c фкыруевмило":
            case "/c Milonov":
            case "/c ANYA II. BACK TO DOTA":
            case "/c Bird is the word": {
                try {
                    Response response = requestHandler.process(text);

                    String message = text.substring(3) + ": status - " + response.getPersonalState() + ", game - " + response.getGameName();
                    sendMsg(chatIdForReply, message);
                    logger.info("Запрос по " + text + " от " + userName + " | " + firstName + " | " + lastName);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }

            }

        }
    }

    private void sendButtonsWithCommands(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери команду:");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/last");
        row.add("/check");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    private void sendButtonsWithNicksForLast(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери ник:");

        // Объект кнопок
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Сами кнопки \ лист рядов кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Создать один ряд кнопок
        KeyboardRow row = new KeyboardRow();
        // Кнопки
        row.add("/l Making Cookies Having Teas.");
        row.add("/l ATHLETE!");
        row.add("/l фкыруевмило");
        // Добавляем в лист
        keyboard.add(row);
        // Второя ряд
        row = new KeyboardRow();
        // Кнопки
        row.add("/l Milonov");
        row.add("/l ANYA II. BACK TO DOTA");
        row.add("/l Bird is the word");
        // Добавить в лист
        keyboard.add(row);
        // Добавить лист в объект
        keyboardMarkup.setKeyboard(keyboard);
        // Положить всё в сообщение
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    // некрасивое дублирование, но, кажется, красиво реализовать это не позволяет апи телеграма
    // ну или делать свою реализацию листа именно для кнопок
    private void sendButtonsWithNicksForCheck(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери ник:");

        // Объект кнопок
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Сами кнопки \ лист рядов кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Создать один ряд кнопок
        KeyboardRow row = new KeyboardRow();
        // Кнопки
        row.add("/c Making Cookies Having Teas.");
        row.add("/c ATHLETE!");
        row.add("/c фкыруевмило");
        // Добавляем в лист
        keyboard.add(row);
        // Второя ряд
        row = new KeyboardRow();
        // Кнопки
        row.add("/c Milonov");
        row.add("/c ANYA II. BACK TO DOTA");
        row.add("/c Bird is the word");
        // Добавить в лист
        keyboard.add(row);
        // Добавить лист в объект
        keyboardMarkup.setKeyboard(keyboard);
        // Положить всё в сообщение
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
            e.printStackTrace();
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
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "Botik";
    }

    public String getBotToken() {
        return "674228669:AAGo5Rw0URRDDrjyzGztO--GM14JG6Y7s9M";
    }
}