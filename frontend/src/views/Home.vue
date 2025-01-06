<template>
    <div id="app">
        <!-- Hero Section -->
        <section class="hero is-fullheight">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title is-size-1-desktop is-size-3-mobile animate__animated animate__fadeInDown">
                        Transforme Seu Casamento em Momentos Mágicos
                    </h1>
                    <p class="subtitle is-size-4-desktop is-size-6-mobile animate__animated animate__fadeInUp">
                        Com o Mural dos Noivos, seus convidados compartilham fotos e vídeos em tempo real,
                        criando um álbum colaborativo único e emocionante durante sua celebração.
                    </p>
                    <div class="buttons is-centered animate__animated animate__fadeInUp animate__delay-1s">
                        <button class="button is-light is-large">Ver Demonstração</button>
                        <router-link to="/auth/noivos/signup?uuid=noivos">
                            <button class="button is-accent is-large">Começar Agora</button>
                        </router-link>

                    </div>
                </div>
            </div>
        </section>

        <!-- Features Section -->
        <section class="section">
            <div class="container">
                <h2 class="title is-2 has-text-centered mb-6">Por que escolher o Mural dos Noivos?</h2>
                <div class="columns is-multiline">
                    <div class="column is-4">
                        <div class="feature-card">
                            <div class="feature-icon">📸</div>
                            <h3 class="title is-4">Compartilhamento Instantâneo</h3>
                            <p>Suas fotos aparecem no telão em tempo real, criando momentos de surpresa e alegria
                                durante toda a festa.</p>
                        </div>
                    </div>
                    <div class="column is-4">
                        <div class="feature-card">
                            <div class="feature-icon">🎥</div>
                            <h3 class="title is-4">Vídeos ao Vivo</h3>
                            <p>Capture pequenos vídeos especiais que são exibidos instantaneamente para todos os
                                convidados.</p>
                        </div>
                    </div>
                    <div class="column is-4">
                        <div class="feature-card">
                            <div class="feature-icon">💝</div>
                            <h3 class="title is-4">Mensagens de Amor</h3>
                            <p>Seus convidados podem deixar mensagens carinhosas que ficam guardadas para sempre.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Statistics Section -->
        <section class="stats-container">
            <div class="container">
                <div class="columns has-text-centered">
                    <div class="column">
                        <div class="stat-value">10.000+</div>
                        <div class="stat-label">Casamentos Realizados</div>
                    </div>
                    <div class="column">
                        <div class="stat-value">1M+</div>
                        <div class="stat-label">Fotos Compartilhadas</div>
                    </div>
                    <div class="column">
                        <div class="stat-value">98%</div>
                        <div class="stat-label">Clientes Satisfeitos</div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Testimonials Section -->
        <section class="section">
            <div class="container">
                <h2 class="title is-2 has-text-centered mb-6">Histórias de Amor Reais</h2>
                <div class="columns">
                    <div class="column is-4">
                        <div class="testimonial-card">
                            <img src="/assets/profile-9.jpeg" alt="Couple" class="testimonial-image">
                            <p class="has-text-centered mb-4">"O mural tornou nossa festa ainda mais especial. Ver as
                                fotos aparecendo em tempo real foi mágico!"</p>
                            <p class="has-text-centered has-text-weight-bold">Maria & João</p>
                            <p class="has-text-centered has-text-grey">Casamento em São Paulo</p>
                        </div>
                    </div>
                    <div class="column is-4">
                        <div class="testimonial-card">
                            <img src="/assets/profile-31.jpeg" alt="Couple" class="testimonial-image">
                            <p class="has-text-centered mb-4">"Nossos convidados adoraram participar! As mensagens nos
                                emocionaram muito."</p>
                            <p class="has-text-centered has-text-weight-bold">Ana & Pedro</p>
                            <p class="has-text-centered has-text-grey">Casamento no Rio</p>
                        </div>
                    </div>
                    <div class="column is-4">
                        <div class="testimonial-card">
                            <img src="/assets/profile-33.jpeg" alt="Couple" class="testimonial-image">
                            <p class="has-text-centered mb-4">"Vale cada centavo! As lembranças que criamos são
                                verdadeiramente únicas."</p>
                            <p class="has-text-centered has-text-weight-bold">Carla & Lucas</p>
                            <p class="has-text-centered has-text-grey">Casamento em Floripa</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Pricing Section -->
        <section class="section has-background-light">
            <div class="container">
                <div class="columns is-centered">
                    <div class="column is-8">
                        <div class="box has-text-centered">
                            <h2 class="title is-2">Pacote Completo</h2>
                            <!-- <div class="price-tag is-size-4">R$299</div> -->
                            <ul class="benefits-list has-text-left mt-5">
                                <li>Mural digital personalizado</li>
                                <li>Compartilhamento ilimitado de fotos</li>
                                <li>Upload de vídeos em tempo real</li>
                                <li>Mensagens dos convidados</li>
                                <li>Suporte técnico durante todo o evento</li>
                                <li>Download de todas as memórias após a festa</li>
                            </ul>
                            <button class="button is-accent is-large mt-5">Garantir Minha Data</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Footer -->
        <footer class="footer">
            <div class="container has-text-centered">
                <p>&copy; 2025 Mural dos Noivos • Transformando Momentos em Memórias Eternas</p>
            </div>
        </footer>
    </div>
</template>

<script>
export default {
    name: 'MuralDosNoivos', 
    mounted(){
        const keysToRemove = [
            'accessToken',
            'username',
            'userId',
            'roles',
            'uuid',
            'wedding_data_id'
        ];

        keysToRemove.forEach(key => localStorage.removeItem(key));
    }
};
</script>

<style>
:root {
    --primary-color: #c991a4;
    /* Rich navy blue */
    --secondary-color: #ffffff;
    --accent-color: #d69d8b;
    /* Warm gold */
    --text-color: #4a4a4a;
    --gradient-start: #d73d5d;
    --gradient-end: #e0a6b2;

}

body {
    font-family: 'Georgia', serif;
    color: var(--text-color);
    background-color: var(--secondary-color);
}

.hero {
    background: linear-gradient(135deg, var(--gradient-start) 0%, var(--gradient-end) 100%);
    position: relative;
    overflow: hidden;
}

.hero::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('https://img.cancaonova.com/cnimages/canais/uploads/sites/6/2017/01/formacao_1600x1200-o-que-os-noivos-precisam-saber-antes-de-se-casarem-768x576.jpg') center/cover;

    opacity: 0.1;
    mix-blend-mode: overlay;
}

.title {
    font-family: 'Georgia', serif;
    font-weight: 700;
    letter-spacing: -0.5px;
}

.hero .title {
    color: white;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.hero .subtitle {
    color: rgba(255, 255, 255, 0.9);
    max-width: 700px;
    margin: 1.5rem auto;
    line-height: 1.6;
}

.button {
    transition: all 0.3s ease;
    text-transform: uppercase;
    font-weight: 600;
    letter-spacing: 1px;
}

.button.is-light {
    background-color: white;
    color: var(--primary-color);
    border: none;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.button.is-accent {
    background-color: var(--accent-color);
    color: white;
    border: none;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.button.is-light:hover,
.button.is-accent:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.feature-card {
    background: white;
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    height: 100%;
}

.feature-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.feature-icon {
    font-size: 2.5rem;
    margin-bottom: 1rem;
    color: var(--primary-color);
}

.testimonial-card {
    background: white;
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    position: relative;
    margin-top: 2rem;
}

.testimonial-image {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    position: absolute;
    top: -40px;
    left: 50%;
    transform: translateX(-50%);
    border: 4px solid white;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stats-container {
    background: linear-gradient(135deg, var(--gradient-start) 0%, var(--gradient-end) 100%);
    padding: 4rem 0;
    color: white;
}

.stat-value {
    font-size: 3rem;
    font-weight: 700;
    margin-bottom: 0.5rem;
}

.stat-label {
    font-size: 1.1rem;
    opacity: 0.9;
}

footer {
    background-color: var(--text-color);
    color: white;
    padding: 2rem 0;
}
</style>