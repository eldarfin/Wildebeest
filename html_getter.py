from selenium import webdriver
from selenium.webdriver.common.keys import Keys

def get_html():
    driver = webdriver.Firefox()
    driver.get("https://www.nytimes.com/crosswords/game/mini")
    assert "The New York Times" in driver.title
    modal_button = driver.find_element_by_class_name('buttons-modalButton--1REsR').click()
    reveal_button = driver.find_element_by_xpath("/html/body/div[@id='root']/div[@class='app-appWrapper--2PSLL']/div[@class='app-app--_ozdu app-displayFooter--1mrFd']/div[@class='app-mainContainer--3CJGG']/div[@class='app-pageContent--AoCNq']/main[@class='CrosswordPage-franklin--7AtYc CrosswordPage-focused--2Phhf']/div[@class='layout']/div[@class='Layout-unveilable--3OmrG']/div[@class='Toolbar-wrapper--1S7nZ']/ul[@class='Toolbar-tools--2qUqg']/div[@class='Toolbar-expandedMenu--2s4M4']/li[@class='Tool-button--39W4J Tool-tool--Fiz94 Tool-texty--2w4Br'][2]/button").click()
    reveal_puzzle_button = driver.find_element_by_xpath("/html/body/div[@id='root']/div[@class='app-appWrapper--2PSLL']/div[@class='app-app--_ozdu app-displayFooter--1mrFd']/div[@class='app-mainContainer--3CJGG']/div[@class='app-pageContent--AoCNq']/main[@class='CrosswordPage-franklin--7AtYc CrosswordPage-focused--2Phhf']/div[@class='layout']/div[@class='Layout-unveilable--3OmrG']/div[@class='Toolbar-wrapper--1S7nZ']/ul[@class='Toolbar-tools--2qUqg']/div[@class='Toolbar-expandedMenu--2s4M4']/li[@class='Tool-button--39W4J Tool-tool--Fiz94 Tool-texty--2w4Br Tool-open--1Moaq']/ul[@class='HelpMenu-menu--1Z_OA']/li[@class='HelpMenu-item--1xl0_'][3]/a").click()
    confirm_modal_button = driver.find_element_by_xpath("/html/body/div[@id='root']/div[@class='app-appWrapper--2PSLL']/div[@class='ModalWrapper-wrapper--1GgyB ModalWrapper-stretch--19Bif']/div[@class='ModalBody-body--3PkKz']/article[@class='ModalBody-content--QYNuF']/div[@class='buttons-modalButtonContainer--35RTh']/button[@class='buttons-modalButton--1REsR']/div/span").click()
    close_modal_button = driver.find_element_by_xpath("/html/body/div[@id='root']/div[@class='app-appWrapper--2PSLL']/div[@class='ModalWrapper-wrapper--1GgyB ModalWrapper-stretch--19Bif']/div[@class='ModalBody-body--3PkKz']/div[@class='ModalBody-closeX--2Fmp7']/a").click()

    html_source = driver.page_source
    driver.close()
    return html_source

print(get_html())
